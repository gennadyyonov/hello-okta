package lv.gennadyyonov.hellookta.bff.graphql.query;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.config.HttpGraphQlTesterFactory;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.bff.test.chucknorris.ChuckNorris;
import lv.gennadyyonov.hellookta.test.user.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DefaultIntegrationTest
class QueryTest {

  @Autowired private HttpGraphQlTesterFactory graphQlTesterFactory;
  @Autowired private ChuckNorris chuckNorris;

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
}
