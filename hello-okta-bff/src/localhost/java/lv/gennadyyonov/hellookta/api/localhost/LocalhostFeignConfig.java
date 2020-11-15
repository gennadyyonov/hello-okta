package lv.gennadyyonov.hellookta.api.localhost;

import brave.http.HttpTracing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.gennadyyonov.hellookta.config.feign.FeignClientProvider;
import lv.gennadyyonov.hellookta.config.feign.LocalhostFeignClientProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class LocalhostFeignConfig {

    private final HttpTracing httpTracing;

    @PostConstruct
    void init() {
        log.info("Initializing Localhost Feign Configuration...");
    }

    @Primary
    @Bean
    public FeignClientProvider feignClientProvider() {
        return new LocalhostFeignClientProvider(httpTracing);
    }
}
