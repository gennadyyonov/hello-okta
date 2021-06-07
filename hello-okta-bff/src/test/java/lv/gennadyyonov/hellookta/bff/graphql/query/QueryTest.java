package lv.gennadyyonov.hellookta.bff.graphql.query;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.bff.test.chucknorris.ChuckNorris;
import lv.gennadyyonov.hellookta.test.user.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DefaultIntegrationTest
class QueryTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;
    @Autowired
    private ChuckNorris chuckNorris;

    @UserInfo
    @SneakyThrows
    @Test
    void ping() {
        chuckNorris.onGetRandomJoke()
            .expect()
            .header("Content-Type", APPLICATION_JSON_VALUE)
            .bodyFile("chuck-norris/randomJoke.json")
            .endStubbing();

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/ping.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.get("$.data.ping")).isEqualTo("Chuck Norris can skydive into outer space.");
    }

}