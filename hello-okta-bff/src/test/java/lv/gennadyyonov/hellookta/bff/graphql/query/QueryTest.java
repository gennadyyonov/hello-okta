package lv.gennadyyonov.hellookta.bff.graphql.query;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.config.DefaultIntegrationTestBase;
import lv.gennadyyonov.hellookta.bff.test.chucknorris.ChuckNorris;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class QueryTest extends DefaultIntegrationTestBase {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;
    @Autowired
    private ChuckNorris chuckNorris;

    @SneakyThrows
    @Test
    void ping() {
        chuckNorris.stubFor(
            WireMock.get("/chuck-norris/jokes/random")
                .willReturn(aResponse()
                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                    .withBodyFile("chuck-norris/randomJoke.json"))
        );

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/ping.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.get("$.data.ping")).isEqualTo("Chuck Norris can skydive into outer space.");
    }

}