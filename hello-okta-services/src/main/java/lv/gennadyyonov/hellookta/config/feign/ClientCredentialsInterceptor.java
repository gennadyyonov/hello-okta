package lv.gennadyyonov.hellookta.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lv.gennadyyonov.hellookta.dto.RunAsDetails;
import lv.gennadyyonov.hellookta.services.TokenService;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

public class ClientCredentialsInterceptor implements RequestInterceptor {

  private final TokenService tokenService;
  private final RunAsDetails runAsDetails;

  public ClientCredentialsInterceptor(TokenService tokenService, RunAsDetails runAsDetails) {
    this.tokenService = tokenService;
    this.runAsDetails = runAsDetails;
  }

  @Override
  public void apply(RequestTemplate template) {
    String accessToken = tokenService.getClientCredentialsAccessToken(runAsDetails);
    template.header(AUTHORIZATION, format("%s %s", BEARER.getValue(), accessToken));
  }
}
