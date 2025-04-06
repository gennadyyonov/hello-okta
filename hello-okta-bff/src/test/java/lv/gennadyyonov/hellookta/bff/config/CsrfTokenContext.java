package lv.gennadyyonov.hellookta.bff.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.bff.test.api.CsrfToken;
import lv.gennadyyonov.hellookta.config.csrf.CsrfProperties;
import lv.gennadyyonov.hellookta.test.setup.Initializable;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Getter
@Component
public class CsrfTokenContext implements Initializable {

  private final CsrfProperties csrfProperties;

  private final AtomicReference<CsrfToken> csrfTokenRef = new AtomicReference<>(null);

  @Override
  public void setUp() {
    var tokenValue = UUID.randomUUID().toString();
    setUp(tokenValue, tokenValue);
  }

  public void setUp(String headerValue, String cookieValue) {
    var csrfToken =
        new CsrfToken(
            csrfProperties.getHeaderName(),
            csrfProperties.getCookieName(),
            headerValue,
            cookieValue);
    csrfTokenRef.set(csrfToken);
  }

  @Override
  public void reset() {
    csrfTokenRef.set(null);
  }
}
