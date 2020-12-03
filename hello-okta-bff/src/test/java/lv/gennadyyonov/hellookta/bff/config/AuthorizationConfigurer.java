package lv.gennadyyonov.hellookta.bff.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Predicate;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Component
public class AuthorizationConfigurer {

    private static final String DEFAULT_USERNAME = "john.doe@gmail.com";
    private static final String DEFAULT_ROLE = "HelloOkta_StandardUser";

    private final TestRestTemplate testRestTemplate;
    private final WireMockServer wireMockServer;

    public void setUp() {
        setUp(DEFAULT_USERNAME, singletonList(DEFAULT_ROLE));
    }

    public void setUp(String username, List<String> groups) {
        reset();
        ofNullable(testRestTemplate.getRestTemplate())
                .ifPresent(restTemplate -> addAuthorizationHeaderInterceptor(restTemplate, username, groups));
        wireMockServer.stubFor(
                WireMock.get("/okta/oauth2/default/v1/userinfo")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                                .withBodyFile("okta/oauth2/" + username.toLowerCase() + ".json"))
        );
    }

    private void reset() {
        ofNullable(testRestTemplate.getRestTemplate()).ifPresent(this::removeAuthorizationHeaderInterceptor);
    }

    private void addAuthorizationHeaderInterceptor(RestTemplate restTemplate, String username, List<String> groups) {
        if (hasAuthorizationHeaderInterceptor(restTemplate)) {
            return;
        }
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors.add(new AuthorizationHeaderInterceptor(username, groups));
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
