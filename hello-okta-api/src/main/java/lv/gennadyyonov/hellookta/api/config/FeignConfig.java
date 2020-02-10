package lv.gennadyyonov.hellookta.api.config;

import brave.http.HttpTracing;
import lv.gennadyyonov.hellookta.configuration.feign.FeignClientProvider;
import lv.gennadyyonov.hellookta.configuration.feign.FeignClientProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
