package lv.gennadyyonov.hellookta.bff.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient.Builder;

import java.util.function.Consumer;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Component
public class GraphqlWebTestClientCustomizer implements Consumer<Builder> {

  private final CsrfTokenContext csrfTokenContext;

  @Override
  public void accept(Builder builder) {
    var csrfTokenRef = csrfTokenContext.getCsrfTokenRef();
    ofNullable(csrfTokenRef.get())
        .ifPresent(
            csrfToken ->
                builder.defaultCookie(csrfToken.getCookieName(), csrfToken.getCookieValue()));
  }
}
