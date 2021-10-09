package lv.gennadyyonov.hellookta.bff.config.graphql;

import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static graphql.schema.AsyncDataFetcher.async;
import static lv.gennadyyonov.hellookta.bff.config.graphql.AsyncContextUtils.createAsyncContext;

@ConditionalOnProperty(prefix = "graphql.task", name = "async-mode-enabled", havingValue = "true")
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
        return new LinkedBlockingQueue<>() {

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
                ExecutorService delegate = new ContextCopyingDelegateExecutorService(asyncContext, threadPoolExecutor);
                ExecutorService executor = new TraceableExecutorService(beanFactory, delegate);
                return async(dataFetcher, executor);
            }
        };
    }
}
