package lv.gennadyyonov.hellookta.bff.config;

import com.google.common.util.concurrent.ForwardingExecutorService;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static graphql.schema.AsyncDataFetcher.async;
import static java.util.Optional.ofNullable;
import static org.slf4j.MDC.getCopyOfContextMap;

@EnableConfigurationProperties(GraphQLTaskProperties.class)
@Configuration
public class GraphQLAsyncConfig {

    private static final String TASK_EXECUTOR_NAME = "graphQLThreadPoolTaskExecutor";

    @Bean(TASK_EXECUTOR_NAME)
    public ThreadPoolTaskExecutor taskExecutor(GraphQLTaskProperties graphQLTaskProperties) {
        BlockingQueue<Runnable> queue = queue();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor() {

            @SuppressWarnings("NullableProblems")
            @Override
            protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
                return queue;
            }
        };
        graphQLTaskProperties.populate(taskExecutor);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }

    /**
     * Creates queue for GraphQL async Thread Pool.
     * Extends {@link LinkedBlockingQueue#offer(Object)} to return false always (no queue == no deadlocks).
     */
    private BlockingQueue<Runnable> queue() {
        return new LinkedBlockingQueue<Runnable>() {

            /**
             * Do not use the queue.
             * If all the threads are working, then the caller thread should execute the code in its own thread (serially).
             */
            @SuppressWarnings("NullableProblems")
            @Override
            public boolean offer(Runnable runnable) {
                return false;
            }
        };
    }

    @Bean
    public Instrumentation instrumentation(BeanFactory beanFactory,
                                           @Qualifier(TASK_EXECUTOR_NAME) ThreadPoolTaskExecutor taskExecutor) {
        return new SimpleInstrumentation() {
            @Override
            public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
                AsyncContext asyncContext = createAsyncContext();
                ThreadPoolExecutor threadPoolExecutor = taskExecutor.getThreadPoolExecutor();
                ExecutorService delegate = new ContextCopyingForwardingExecutorService(asyncContext, threadPoolExecutor);
                ExecutorService executor = new TraceableExecutorService(beanFactory, delegate);
                return async(dataFetcher, executor);
            }
        };
    }

    @RequiredArgsConstructor
    private static final class ContextCopyingForwardingExecutorService extends ForwardingExecutorService {

        private final AsyncContext asyncContext;
        private final ExecutorService delegate;

        @Override
        public void execute(Runnable command) {
            Runnable contextCopyingRunnable = new ContextCopyingRunnable(command, asyncContext);
            Runnable runnable = new DelegatingSecurityContextRunnable(contextCopyingRunnable, asyncContext.getSecurityContext());
            super.execute(runnable);
        }

        @Override
        protected ExecutorService delegate() {
            return delegate;
        }
    }

    @RequiredArgsConstructor
    private static final class ContextCopyingRunnable implements Runnable {

        private final Runnable delegate;
        private final AsyncContext delegateAsyncContext;

        @Override
        public void run() {
            AsyncContext originalAsyncContext = createAsyncContext();
            try {
                populate(delegateAsyncContext);
                delegate.run();
            } finally {
                populate(originalAsyncContext);
            }
        }

        private static void populate(AsyncContext asyncContext) {
            asyncContext.getMdcContextMap().forEach(MDC::put);
            RequestContextHolder.setRequestAttributes(asyncContext.getRequestAttributes());
        }
    }

    @Data
    @Builder
    private static final class AsyncContext {

        private Map<String, String> mdcContextMap;
        private RequestAttributes requestAttributes;
        private SecurityContext securityContext;
    }

    private static AsyncContext createAsyncContext() {
        Map<String, String> mdcContextMap = ofNullable(getCopyOfContextMap()).orElseGet(HashMap::new);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return AsyncContext.builder()
                .mdcContextMap(mdcContextMap)
                .requestAttributes(requestAttributes)
                .securityContext(securityContext)
                .build();
    }
}
