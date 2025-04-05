package lv.gennadyyonov.hellookta.bff.config.rest;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.test.user.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DefaultIntegrationTest
class RestExceptionHandlerTest {

  @Autowired private MockMvc mvc;

  @UserInfo
  @Test
  @SneakyThrows
  void handleAccessDeniedException() {
    mvc.perform(get("/test/access-denied-error"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.code").value("HO.ER.ACCESSDENIED"))
        .andExpect(jsonPath("$.message").value("Simulated access denied failure"));
  }

  @UserInfo
  @Test
  @SneakyThrows
  void handleExternalSystemException() {
    mvc.perform(get("/test/external-system-error"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.code").value("HO.ER.EXTERNALSYSTEM"))
        .andExpect(jsonPath("$.message").value("Simulated external system failure"));
  }
}
