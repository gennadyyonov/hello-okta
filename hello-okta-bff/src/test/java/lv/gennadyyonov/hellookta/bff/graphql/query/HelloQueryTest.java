package lv.gennadyyonov.hellookta.bff.graphql.query;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.config.HttpGraphQlTesterFactory;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.bff.test.hellooktaapi.HelloOktaApi;
import lv.gennadyyonov.hellookta.test.user.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DefaultIntegrationTest
class HelloQueryTest {

  @Autowired private HttpGraphQlTesterFactory graphQlTesterFactory;
  @Autowired private HelloOktaApi helloOktaApi;

  @UserInfo
  @SneakyThrows
  @Test
  void hello() {
    helloOktaApi
        .onPostHello()
        .expect()
        .header("Content-Type", APPLICATION_JSON_VALUE)
        .transformers("response-template")
        .bodyFile("hello-okta-api/hello.json")
        .endStubbing();

    var graphQlTester = graphQlTesterFactory.createTester();
    var response =
        graphQlTester
            .documentName("hello")
            .execute()
            .path("data.hello.text")
            .entity(String.class)
            .get();
    assertThat(response).isEqualTo("Hello, JOHN.DOE@GMAIL.COM!");
  }
}
