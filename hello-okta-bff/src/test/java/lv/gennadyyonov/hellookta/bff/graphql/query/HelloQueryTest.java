package lv.gennadyyonov.hellookta.bff.graphql.query;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.config.DefaultIntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static lv.gennadyyonov.hellookta.bff.utils.AuthorizationUtils.authorizationHeader;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class HelloQueryTest extends DefaultIntegrationTestBase {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;
    @Autowired
    private WireMockServer wireMockServer;

    @SneakyThrows
    @Test
    void hello() {
        wireMockServer.stubFor(
                WireMock.get("/hello-okta-api/hello")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                                .withBodyFile("hello-okta-api/hello.json")
                                .withTransformers("response-template"))
        );

        graphQLTestTemplate.addHeader(AUTHORIZATION, authorizationHeader());

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/hello.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.get("$.data.hello.text")).isEqualTo("Hello, JOHN.DOE@GMAIL.COM!");
    }
}