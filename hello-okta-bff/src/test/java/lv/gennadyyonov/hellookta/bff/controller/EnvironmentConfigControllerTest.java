package lv.gennadyyonov.hellookta.bff.controller;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.config.CsrfTokenContext;
import lv.gennadyyonov.hellookta.bff.dto.EnvironmentProperties;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.bff.test.api.TestApiClient;
import lv.gennadyyonov.hellookta.bff.test.okta.Okta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultIntegrationTest
class EnvironmentConfigControllerTest {

  @Autowired private TestApiClient client;
  @Autowired private Okta okta;
  @Autowired private CsrfTokenContext csrfTokenContext;

  @SneakyThrows
  @Test
  void environmentConfig() {
    csrfTokenContext.reset();

    var response = client.getEnvironmentConfig();

    var environmentProperties =
        response.assertStatusIs(HttpStatus.OK).getBody(EnvironmentProperties.class);
    assertThat(environmentProperties)
        .isEqualTo(
            EnvironmentProperties.builder()
                .oktaClientId("0oa4yt76r3KSPx094357")
                .oktaIssuer(okta.getDelegate().baseUrl() + "/okta/oauth2/default")
                .csrfEnabled(true)
                .build());
  }
}
