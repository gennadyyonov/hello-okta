package lv.gennadyyonov.hellookta.bff.graphql.query;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.config.HttpGraphQlTesterFactory;
import lv.gennadyyonov.hellookta.bff.graphql.type.TranslationMap;
import lv.gennadyyonov.hellookta.bff.graphql.type.TranslationMapEntry;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.test.user.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultIntegrationTest
class TranslationQueryTest {

  @Autowired private HttpGraphQlTesterFactory graphQlTesterFactory;

  @UserInfo
  @SneakyThrows
  @Test
  void translationMap() {
    var graphQlTester = graphQlTesterFactory.createTester();
    var response =
        graphQlTester
            .documentName("translationMap")
            .execute()
            .path("data.translationMap")
            .entity(TranslationMap.class)
            .get();

    assertThat(response.getLocale()).isEqualTo("en");
    List<TranslationMapEntry> entries = response.getEntries();
    assertThat(entries)
        .containsExactlyInAnyOrder(
            TranslationMapEntry.builder().key("home_button_ping").value("Ping").build(),
            TranslationMapEntry.builder()
                .key("logout_hint")
                .value("Pressing 'Logout' button will sign current user out")
                .build(),
            TranslationMapEntry.builder().key("button_logout").value("Logout").build(),
            TranslationMapEntry.builder()
                .key("SC.ER.ACCESSDENIED")
                .value("You don't have access rights to application.")
                .build());
  }
}
