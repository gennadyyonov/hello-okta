package lv.gennadyyonov.hellookta.api.localhost;

import brave.http.HttpTracing;
import lv.gennadyyonov.hellookta.configuration.feign.FeignClientProvider;
import lv.gennadyyonov.hellookta.configuration.feign.LocalhostFeignClientProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class LocalhostFeignConfig {

    private final HttpTracing httpTracing;

    @Autowired
    public LocalhostFeignConfig(HttpTracing httpTracing) {
        this.httpTracing = httpTracing;
    }

    @Primary
    @Bean
    public FeignClientProvider feignClientProvider() {
        return new LocalhostFeignClientProvider(httpTracing);
    }
}
