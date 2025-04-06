package lv.gennadyyonov.hellookta.bff.config.rest;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.bff.test.api.TestApiClient;
import lv.gennadyyonov.hellookta.bff.test.api.TestApiResult;
import lv.gennadyyonov.hellookta.exception.ErrorResponse;
import lv.gennadyyonov.hellookta.test.user.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultIntegrationTest
class RestExceptionHandlerTest {

  @Autowired private TestApiClient client;

  @UserInfo
  @Test
  @SneakyThrows
  void handleAccessDeniedException() {
    TestApiResult result = client.doGet("/test/access-denied-error");
    ErrorResponse errorResponse =
        result.assertStatusIs(HttpStatus.FORBIDDEN).getBody(ErrorResponse.class);
    assertThat(errorResponse)
        .isEqualTo(
            ErrorResponse.builder()
                .code("HO.ER.ACCESSDENIED")
                .message("Simulated access denied failure")
                .build());
  }

  @UserInfo
  @Test
  @SneakyThrows
  void handleExternalSystemException() {
    TestApiResult result = client.doGet("/test/external-system-error");
    ErrorResponse errorResponse =
        result.assertStatusIs(HttpStatus.INTERNAL_SERVER_ERROR).getBody(ErrorResponse.class);
    assertThat(errorResponse)
        .isEqualTo(
            ErrorResponse.builder()
                .code("HO.ER.EXTERNALSYSTEM")
                .message("Simulated external system failure")
                .build());
  }
}
