package lv.gennadyyonov.hellookta.test.user;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@TestComponent
public class AuthUserInfoConfig implements UserInfoConfig {

    private final TestRestTemplate testRestTemplate;

    @Override
    public void setUp(String username, List<String> groups) {
        ofNullable(testRestTemplate.getRestTemplate())
            .ifPresent(restTemplate -> addAuthHeaderInterceptor(restTemplate, username, groups));
    }

    @Override
    public void reset() {
        ofNullable(testRestTemplate.getRestTemplate()).ifPresent(this::removeAuthHeaderInterceptor);
    }

    private void addAuthHeaderInterceptor(RestTemplate restTemplate, String username, List<String> groups) {
        if (hasAuthHeaderInterceptor(restTemplate)) {
            return;
        }
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors.add(new AuthHeaderInterceptor(username, groups));
        restTemplate.setInterceptors(interceptors);
    }

    private void removeAuthHeaderInterceptor(RestTemplate restTemplate) {
        restTemplate.getInterceptors().removeIf(oktaAuthHeaderInterceptor());
    }

    private boolean hasAuthHeaderInterceptor(RestTemplate restTemplate) {
        return restTemplate.getInterceptors().stream().anyMatch(oktaAuthHeaderInterceptor());
    }

    private Predicate<ClientHttpRequestInterceptor> oktaAuthHeaderInterceptor() {
        return interceptor -> interceptor instanceof AuthHeaderInterceptor;
    }
}
