package lv.gennadyyonov.hellookta.bff.controller;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.dto.CsrfTokenInfo;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.bff.test.api.TestApiClient;
import lv.gennadyyonov.hellookta.bff.test.api.TestApiResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultIntegrationTest
class CsrfTokenInfoControllerTest {

  @Autowired private TestApiClient client;

  @SneakyThrows
  @Test
  void csrfTokenInfo() {
    TestApiResult result = client.getCsrfTokenInfo();
    var csrfTokenInfo = result.assertStatusIs(HttpStatus.OK).getBody(CsrfTokenInfo.class);
    assertThat(csrfTokenInfo)
        .isEqualTo(
            CsrfTokenInfo.builder().cookieName("XSRF-TOKEN").headerName("X-XSRF-TOKEN").build());
  }
}
