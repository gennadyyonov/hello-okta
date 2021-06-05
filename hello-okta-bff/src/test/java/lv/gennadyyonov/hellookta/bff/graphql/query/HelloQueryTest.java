package lv.gennadyyonov.hellookta.bff.graphql.query;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.bff.test.hellooktaapi.HelloOktaApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DefaultIntegrationTest
class HelloQueryTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;
    @Autowired
    private HelloOktaApi helloOktaApi;

    @SneakyThrows
    @Test
    void hello() {
        helloOktaApi.stubFor(
            WireMock.get("/hello-okta-api/hello")
                .willReturn(aResponse()
                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                    .withBodyFile("hello-okta-api/hello.json")
                    .withTransformers("response-template"))
        );

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/hello.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.get("$.data.hello.text")).isEqualTo("Hello, JOHN.DOE@GMAIL.COM!");
    }
}