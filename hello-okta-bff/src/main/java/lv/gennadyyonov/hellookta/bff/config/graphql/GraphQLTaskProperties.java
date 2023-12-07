package lv.gennadyyonov.hellookta.bff.config.graphql;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import static java.util.Optional.ofNullable;

@ConditionalOnProperty(prefix = "graphql.task", name = "async-mode-enabled", havingValue = "true")
@ConfigurationProperties(prefix = "graphql.task")
@RequiredArgsConstructor
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
    private static class Execution {

        String threadNamePrefix;
        @NotNull
        @Valid
        Pool pool;
    }

    @RequiredArgsConstructor
    @Getter
    @Value
    private static class Pool {

        @Min(MIN_POOL_CORE_SIZE)
        Integer coreSize;
        @NotNull
        @Min(MIN_POOL_MAX_SIZE)
        Integer maxSize;
        @Min(MIN_POOL_KEEP_ALIVE_SECONDS)
        Integer keepAliveSeconds;
    }
}
