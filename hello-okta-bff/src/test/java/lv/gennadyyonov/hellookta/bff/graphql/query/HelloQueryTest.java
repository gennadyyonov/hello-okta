package lv.gennadyyonov.hellookta.bff.graphql.query;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.bff.test.hellooktaapi.HelloOktaApi;
import lv.gennadyyonov.hellookta.test.user.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DefaultIntegrationTest
class HelloQueryTest {

  @Autowired private GraphQLTestTemplate graphQLTestTemplate;
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

    GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/hello.graphql");

    assertThat(response.isOk()).isTrue();
    assertThat(response.get("$.data.hello.text")).isEqualTo("Hello, JOHN.DOE@GMAIL.COM!");
  }
}
