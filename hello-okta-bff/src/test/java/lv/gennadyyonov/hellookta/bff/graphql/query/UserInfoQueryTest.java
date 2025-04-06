package lv.gennadyyonov.hellookta.bff.graphql.query;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.config.CsrfTokenContext;
import lv.gennadyyonov.hellookta.bff.config.HttpGraphQlTesterFactory;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.bff.test.api.TestApiClient;
import lv.gennadyyonov.hellookta.bff.test.okta.Okta;
import lv.gennadyyonov.hellookta.bff.test.user.MoonChild;
import lv.gennadyyonov.hellookta.test.user.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultIntegrationTest
class UserInfoQueryTest {

  @Autowired private HttpGraphQlTesterFactory graphQlTesterFactory;
  @Autowired private Okta okta;
  @Autowired private TestApiClient client;
  @Autowired private CsrfTokenContext csrfTokenContext;

  @Value("classpath:graphql-test/me.graphql")
  private Resource me;

  @UserInfo("jane.smith@gmail.com")
  @SneakyThrows
  @Test
  void me() {
    var graphQlTester = graphQlTesterFactory.createTester();
    var response =
        graphQlTester
            .documentName("me")
            .execute()
            .path("data.me")
            .entity(lv.gennadyyonov.hellookta.dto.UserInfo.class)
            .get();

    assertThat(response.getUserId()).isEqualTo("JANE.SMITH@GMAIL.COM");
    assertThat(response.getFirstName()).isEqualTo("Jane");
    assertThat(response.getLastName()).isEqualTo("Smith");
    assertThat(response.getEmail()).isEqualTo("Jane.Smith@gmail.com");
    var roles = response.getRoles();
    assertThat(roles)
        .containsExactlyInAnyOrder(
            "HelloOkta_StandardUser", "SCOPE_openid", "SCOPE_email", "SCOPE_profile");
  }

  @MoonChild
  @SneakyThrows
  @Test
  void forbidden() {
    var graphQlTester = graphQlTesterFactory.createTester();
    graphQlTester
        .documentName("me")
        .execute()
        .errors()
        .satisfy(
            errors -> {
              assertThat(errors).hasSize(1);
              var error = errors.getFirst();
              assertThat(error.getErrorType()).isEqualTo(ErrorType.FORBIDDEN);
              assertThat(error.getMessage())
                  .isEqualTo("User 'MOON.CHILD@GMAIL.COM' has no access to UserInfoQuery.me()");
              assertThat(error.getPath()).isEqualTo("me");
              assertThat(error.getExtensions())
                  .containsAllEntriesOf(
                      Map.of("code", "HO.ER.ACCESSDENIED", "classification", "FORBIDDEN"));
            });
  }

  @UserInfo
  @SneakyThrows
  @Test
  void externalSystemError() {
    okta.onGetUserInfo().expect().status(HttpStatus.SERVICE_UNAVAILABLE).endStubbing();

    var graphQlTester = graphQlTesterFactory.createTester();
    graphQlTester
        .documentName("me")
        .execute()
        .errors()
        .satisfy(
            errors -> {
              assertThat(errors).hasSize(1);
              var error = errors.getFirst();
              assertThat(error.getErrorType()).isEqualTo(ErrorType.INTERNAL_ERROR);
              assertThat(error.getMessage()).isEqualTo("Failed to fetch user info from Okta");
              assertThat(error.getPath()).isEqualTo("me");
              assertThat(error.getExtensions())
                  .containsAllEntriesOf(
                      Map.of("code", "HO.ER.EXTERNALSYSTEM", "classification", "INTERNAL_ERROR"));
            });
  }

  @UserInfo
  @SneakyThrows
  @Test
  void missingCsrfToken() {
    csrfTokenContext.reset();

    var result = client.executeGraphqlQuery(me);

    result.assertStatusIs(HttpStatus.FORBIDDEN);
  }

  @UserInfo
  @SneakyThrows
  @Test
  void invalidCsrfToken() {
    csrfTokenContext.setUp("headerValue", "cookieValue");

    var result = client.executeGraphqlQuery(me);

    result.assertStatusIs(HttpStatus.FORBIDDEN);
  }
}
