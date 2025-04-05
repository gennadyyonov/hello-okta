package lv.gennadyyonov.hellookta.bff.graphql.query;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.config.HttpGraphQlTesterFactory;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.bff.test.chucknorris.ChuckNorris;
import lv.gennadyyonov.hellookta.bff.test.okta.Okta;
import lv.gennadyyonov.hellookta.bff.test.user.MoonChild;
import lv.gennadyyonov.hellookta.test.user.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DefaultIntegrationTest
class QueryTest {

  @Autowired private HttpGraphQlTesterFactory graphQlTesterFactory;
  @Autowired private ChuckNorris chuckNorris;
  @Autowired private Okta okta;

  @UserInfo
  @SneakyThrows
  @Test
  void ping() {
    chuckNorris
        .onGetRandomJoke()
        .expect()
        .header("Content-Type", APPLICATION_JSON_VALUE)
        .bodyFile("chuck-norris/randomJoke.json")
        .endStubbing();

    var graphQlTester = graphQlTesterFactory.createTester();
    var response =
        graphQlTester.documentName("ping").execute().path("data.ping").entity(String.class).get();
    assertThat(response).isEqualTo("Chuck Norris can skydive into outer space.");
  }

  @MoonChild
  @SneakyThrows
  @Test
  void forbidden() {
    var graphQlTester = graphQlTesterFactory.createTester();
    graphQlTester
        .documentName("ping")
        .execute()
        .errors()
        .satisfy(
            errors -> {
              assertThat(errors).hasSize(1);
              var error = errors.getFirst();
              assertThat(error.getErrorType()).isEqualTo(ErrorType.FORBIDDEN);
              assertThat(error.getMessage())
                  .isEqualTo("User 'MOON.CHILD@GMAIL.COM' has no access to Query.ping()");
              assertThat(error.getPath()).isEqualTo("ping");
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
        .documentName("ping")
        .execute()
        .errors()
        .satisfy(
            errors -> {
              assertThat(errors).hasSize(1);
              var error = errors.getFirst();
              assertThat(error.getErrorType()).isEqualTo(ErrorType.INTERNAL_ERROR);
              assertThat(error.getMessage()).isEqualTo("Failed to fetch user info from Okta");
              assertThat(error.getPath()).isEqualTo("ping");
              assertThat(error.getExtensions())
                  .containsAllEntriesOf(
                      Map.of("code", "HO.ER.EXTERNALSYSTEM", "classification", "INTERNAL_ERROR"));
            });
  }
}
