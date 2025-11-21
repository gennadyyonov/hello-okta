package lv.gennadyyonov.hellookta.test.user;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientProperties;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@TestComponent
public class AuthUserInfoConfig implements UserInfoConfig {

  private static final String OKTA = "okta";

  private final TestRestTemplate testRestTemplate;
  private final OAuth2ClientProperties oktaOAuth2Properties;

  @Override
  public void setUp(String username, List<String> groups) {
    ofNullable(testRestTemplate.getRestTemplate())
        .ifPresent(restTemplate -> addAuthHeaderInterceptor(restTemplate, username, groups));
  }

  @Override
  public void reset() {
    ofNullable(testRestTemplate.getRestTemplate()).ifPresent(this::removeAuthHeaderInterceptor);
  }

  private void addAuthHeaderInterceptor(
      RestTemplate restTemplate, String username, List<String> groups) {
    if (hasAuthHeaderInterceptor(restTemplate)) {
      return;
    }
    String issuer = getIssuerUri(oktaOAuth2Properties);
    List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
    interceptors.add(new AuthHeaderInterceptor(issuer, username, groups));
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

  private String getIssuerUri(OAuth2ClientProperties properties) {
    return ofNullable(properties)
        .map(OAuth2ClientProperties::getProvider)
        .map(map -> map.get(OKTA))
        .map(OAuth2ClientProperties.Provider::getIssuerUri)
        .orElse(null);
  }
}
