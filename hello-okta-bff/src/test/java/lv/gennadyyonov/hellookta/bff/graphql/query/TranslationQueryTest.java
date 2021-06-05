package lv.gennadyyonov.hellookta.bff.graphql.query;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.graphql.type.TranslationMapEntry;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultIntegrationTest
class TranslationQueryTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @SneakyThrows
    @Test
    void translationMap() {
        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/translationMap.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.get("$.data.translationMap.locale")).isEqualTo("en");
        List<TranslationMapEntry> entries = response.getList("$.data.translationMap.entries", TranslationMapEntry.class);
        assertThat(entries).hasSize(3);
        assertThat(entries)
            .contains(
                TranslationMapEntry.builder()
                    .key("home_button_ping")
                    .value("Ping")
                    .build(),
                TranslationMapEntry.builder()
                    .key("logout_hint")
                    .value("Pressing 'Logout' button will sign current user out")
                    .build(),
                TranslationMapEntry.builder()
                    .key("button_logout")
                    .value("Logout")
                    .build()
            );
    }
}