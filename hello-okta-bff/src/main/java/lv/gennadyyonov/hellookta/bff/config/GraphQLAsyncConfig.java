package lv.gennadyyonov.hellookta.bff.config;

import com.google.common.util.concurrent.ForwardingExecutorService;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.AsyncDataFetcher;
import graphql.schema.DataFetcher;
import org.slf4j.MDC;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import static java.util.Optional.ofNullable;
import static org.slf4j.MDC.getCopyOfContextMap;

@Configuration
public class GraphQLAsyncConfig {

    public GraphQLAsyncConfig() {
        super();
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    public Instrumentation instrumentation(BeanFactory beanFactory) {
        return new SimpleInstrumentation() {
            @Override
            public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
                Map<String, String> mdcContextMap = getCopyOfContextMap();
                ExecutorService delegate = new MDCForwardingExecutorService(mdcContextMap);
                ExecutorService executor = new TraceableExecutorService(beanFactory, delegate);
                return new AsyncDataFetcher<>(dataFetcher, executor);
            }
        };
    }

    private static final class MDCForwardingExecutorService extends ForwardingExecutorService {

        private final Map<String, String> mdcContextMap;
        private final ExecutorService delegate;

        private MDCForwardingExecutorService(Map<String, String> mdcContextMap) {
            this.mdcContextMap = mdcContextMap;
            this.delegate = ForkJoinPool.commonPool();
        }

        @Override
        public void execute(Runnable command) {
            super.execute(new MDCRunnable(command, mdcContextMap));
        }

        @Override
        protected ExecutorService delegate() {
            return delegate;
        }
    }

    private static final class MDCRunnable implements Runnable {

        private final Runnable delegate;
        private final Map<String, String> mdcContextMap;

        private MDCRunnable(Runnable delegate, Map<String, String> mdcContextMap) {
            this.delegate = delegate;
            this.mdcContextMap = mdcContextMap;
        }

        @Override
        public void run() {
            Collection<String> keys = initMDCContext();
            try {
                delegate.run();
            } finally {
                keys.forEach(MDC::remove);
            }
        }

        private Collection<String> initMDCContext() {
            Collection<String> keys = new HashSet<>();
            Map<String, String> currentMdcContextMap = ofNullable(getCopyOfContextMap()).orElse(new HashMap<>());
            mdcContextMap.forEach((key, value) -> {
                if (!currentMdcContextMap.containsKey(key)) {
                    MDC.put(key, value);
                    keys.add(key);
                }
            });
            return keys;
        }
    }
}
