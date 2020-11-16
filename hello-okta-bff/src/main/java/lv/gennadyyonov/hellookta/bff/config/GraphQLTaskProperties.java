package lv.gennadyyonov.hellookta.bff.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static java.util.Optional.ofNullable;

@ConfigurationProperties(prefix = "graphql.task")
@RequiredArgsConstructor
@ConstructorBinding
@Validated
@Getter
public class GraphQLTaskProperties {

    private static final int MIN_POOL_CORE_SIZE = 0;
    private static final int MIN_POOL_MAX_SIZE = 1;
    private static final int MIN_POOL_KEEP_ALIVE_SECONDS = 0;

    @NotNull
    @Valid
    private final Execution execution;

    void populate(ThreadPoolTaskExecutor taskExecutor) {
        ofNullable(execution).map(Execution::getPool).ifPresent(pool -> {
            ofNullable(pool.getCoreSize()).ifPresent(taskExecutor::setCorePoolSize);
            ofNullable(pool.getMaxSize()).ifPresent(taskExecutor::setMaxPoolSize);
            ofNullable(pool.getKeepAliveSeconds()).ifPresent(taskExecutor::setKeepAliveSeconds);
        });
        ofNullable(execution).map(Execution::getThreadNamePrefix).ifPresent(taskExecutor::setThreadNamePrefix);
    }

    @RequiredArgsConstructor
    @Getter
    @Value
    private static final class Execution {

        private final String threadNamePrefix;
        @NotNull
        @Valid
        private final Pool pool;
    }

    @RequiredArgsConstructor
    @Getter
    @Value
    private static final class Pool {

        @Min(MIN_POOL_CORE_SIZE)
        private final Integer coreSize;
        @NotNull
        @Min(MIN_POOL_MAX_SIZE)
        private final Integer maxSize;
        @Min(MIN_POOL_KEEP_ALIVE_SECONDS)
        private final Integer keepAliveSeconds;
    }
}
