package lv.gennadyyonov.hellookta.bff.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Component
public class TestRestTemplateConfigurer {

    private final AuthorizationHeaderInterceptor authorizationHeaderInterceptor;

    public void setUp(TestRestTemplate testRestTemplate) {
        ofNullable(testRestTemplate.getRestTemplate()).ifPresent(this::addAuthorizationHeaderInterceptor);
    }

    public void reset(TestRestTemplate testRestTemplate) {
        ofNullable(testRestTemplate.getRestTemplate()).ifPresent(this::removeAuthorizationHeaderInterceptor);
    }

    private void addAuthorizationHeaderInterceptor(RestTemplate restTemplate) {
        if (hasAuthorizationHeaderInterceptor(restTemplate)) {
            return;
        }
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors.add(authorizationHeaderInterceptor);
        restTemplate.setInterceptors(interceptors);
    }

    private void removeAuthorizationHeaderInterceptor(RestTemplate restTemplate) {
        restTemplate.getInterceptors().removeIf(customInterceptor());
    }

    private boolean hasAuthorizationHeaderInterceptor(RestTemplate restTemplate) {
        return restTemplate.getInterceptors().stream().anyMatch(customInterceptor());
    }

    private Predicate<ClientHttpRequestInterceptor> customInterceptor() {
        return interceptor -> interceptor instanceof AuthorizationHeaderInterceptor;
    }
}
