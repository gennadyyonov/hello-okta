package lv.gennadyyonov.hellookta.config.feign;

import brave.http.HttpTracing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

import static lv.gennadyyonov.hellookta.config.EnvironmentProfiles.LOCALHOST;

@Profile(LOCALHOST)
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
