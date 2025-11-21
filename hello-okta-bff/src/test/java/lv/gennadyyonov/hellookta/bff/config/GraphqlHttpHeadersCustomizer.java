package lv.gennadyyonov.hellookta.bff.config;

import lv.gennadyyonov.hellookta.test.user.JwtToken;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static java.util.Optional.ofNullable;
import static lv.gennadyyonov.hellookta.utils.OktaUtils.getIssuerUri;

@Component
public class GraphqlHttpHeadersCustomizer implements Consumer<HttpHeaders> {

  private final String issuer;
  private final TokenInfoContext tokenInfoContext;
  private final CsrfTokenContext csrfTokenContext;

  public GraphqlHttpHeadersCustomizer(
      OAuth2ClientProperties oktaOAuth2Properties,
      TokenInfoContext tokenInfoContext,
      CsrfTokenContext csrfTokenContext) {
    this.issuer = getIssuerUri(oktaOAuth2Properties);
    this.tokenInfoContext = tokenInfoContext;
    this.csrfTokenContext = csrfTokenContext;
  }

  @Override
  public void accept(HttpHeaders headers) {
    AtomicReference<TokenInfo> tokenInfoRef = tokenInfoContext.getTokenInfoRef();
    ofNullable(tokenInfoRef.get())
        .ifPresent(
            tokenInfo -> {
              String username = tokenInfo.getUsername();
              List<String> groups = tokenInfo.getGroups();
              headers.setBearerAuth(JwtToken.createCompact(issuer, username, groups));
            });
    var csrfTokenRef = csrfTokenContext.getCsrfTokenRef();
    ofNullable(csrfTokenRef.get())
        .ifPresent(
            csrfToken ->
                headers.put(csrfToken.getHeaderName(), List.of(csrfToken.getHeaderValue())));
  }
}
