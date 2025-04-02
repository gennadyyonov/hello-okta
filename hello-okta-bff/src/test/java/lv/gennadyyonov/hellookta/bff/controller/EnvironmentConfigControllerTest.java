package lv.gennadyyonov.hellookta.bff.controller;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.bff.test.okta.Okta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static lv.gennadyyonov.hellookta.bff.controller.EnvironmentConfigController.ENVIRONMENT_CONFIG_SUFFIX;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DefaultIntegrationTest
class EnvironmentConfigControllerTest {

  @Autowired private MockMvc mvc;
  @Autowired private Okta okta;

  @SneakyThrows
  @Test
  void environmentConfig() {
    String baseUrl = okta.getDelegate().baseUrl();
    mvc.perform(get(ENVIRONMENT_CONFIG_SUFFIX))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.oktaClientId").value("0oa4yt76r3KSPx094357"))
        .andExpect(jsonPath("$.oktaIssuer").value(baseUrl + "/okta/oauth2/default"))
        .andExpect(jsonPath("$.csrfEnabled").value(false));
  }
}
