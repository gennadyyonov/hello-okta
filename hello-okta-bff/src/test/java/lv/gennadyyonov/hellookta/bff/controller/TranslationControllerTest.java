package lv.gennadyyonov.hellookta.bff.controller;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.i18n.TranslationMap;
import lv.gennadyyonov.hellookta.bff.i18n.TranslationMapEntry;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.bff.test.api.TestApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultIntegrationTest
class TranslationControllerTest {

  @Autowired private TestApiClient client;

  @SneakyThrows
  @Test
  void translationMap() {
    var response = client.getTranslationMap();

    var translationMap = response.assertStatusIs(HttpStatus.OK).getBody(TranslationMap.class);
    assertThat(translationMap.getLocale()).isEqualTo("en");
    List<TranslationMapEntry> entries = translationMap.getEntries();
    assertThat(entries)
        .containsExactlyInAnyOrder(
            TranslationMapEntry.builder().key("home_button_ping").value("Ping").build(),
            TranslationMapEntry.builder()
                .key("logout_hint")
                .value("Pressing 'Logout' button will sign current user out")
                .build(),
            TranslationMapEntry.builder().key("button_logout").value("Logout").build(),
            TranslationMapEntry.builder().key("button_go_home").value("Go Home").build(),
            TranslationMapEntry.builder()
                .key("HO.ER.ACCESSDENIED")
                .value("You don't have access rights to application.")
                .build(),
            TranslationMapEntry.builder()
                .key("HO.ER.EXTERNALSYSTEM")
                .value(
                    "There was an issue communicating with an external service. Please try again later.")
                .build(),
            TranslationMapEntry.builder()
                .key("HO.ER.UNAUTHORIZED.TITLE")
                .value("Unauthorized Access")
                .build(),
            TranslationMapEntry.builder()
                .key("HO.ER.UNAUTHORIZED.TEXT")
                .value(
                    "You are not authenticated or your session has expired. Please log in again to continue.")
                .build());
  }
}
