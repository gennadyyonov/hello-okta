package lv.gennadyyonov.hellookta.bff.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class TestRestTemplateConfig {

    private final AuthorizationHeaderInterceptor authorizationHeaderInterceptor;

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer() {
        return this::addAuthorizationHeaderInterceptor;
    }

    private void addAuthorizationHeaderInterceptor(RestTemplate restTemplate) {
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors.add(authorizationHeaderInterceptor);
        restTemplate.setInterceptors(interceptors);
    }
}
