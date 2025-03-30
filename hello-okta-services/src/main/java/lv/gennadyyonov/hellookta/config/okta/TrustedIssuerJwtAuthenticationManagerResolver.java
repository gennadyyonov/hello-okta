package lv.gennadyyonov.hellookta.config.okta;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class TrustedIssuerJwtAuthenticationManagerResolver
    implements AuthenticationManagerResolver<String> {

  private final Map<String, AuthenticationManager> trustedAuthenticationManagers;

  @Override
  public AuthenticationManager resolve(String issuer) {
    if (trustedAuthenticationManagers.containsKey(issuer)) {
      var authenticationManager = trustedAuthenticationManagers.get(issuer);
      log.info("Authentication Manager for issuer [{}] resolved", issuer);
      return authenticationManager;
    } else {
      log.error(
          "Failed to resolve Authentication Manager for issuer [{}] since it is not trusted",
          issuer);
    }
    return null;
  }
}
