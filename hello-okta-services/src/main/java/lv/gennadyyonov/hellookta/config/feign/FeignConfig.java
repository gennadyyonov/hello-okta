package lv.gennadyyonov.hellookta.config.feign;

import brave.http.HttpTracing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static lv.gennadyyonov.hellookta.config.EnvironmentProfiles.NOT_LOCALHOST;

@Profile(NOT_LOCALHOST)
@Configuration
public class FeignConfig {

    private final HttpTracing httpTracing;

    @Autowired
    public FeignConfig(HttpTracing httpTracing) {
        this.httpTracing = httpTracing;
    }

    @Bean
    public FeignClientProvider feignClientProvider() {
        return new FeignClientProviderImpl(httpTracing);
    }
}
