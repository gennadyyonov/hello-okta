package lv.gennadyyonov.hellookta.bff.controller;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static lv.gennadyyonov.hellookta.bff.controller.CsrfTokenInfoController.CSRF_TOKEN_INFO_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DefaultIntegrationTest
class CsrfTokenInfoControllerTest {

  @Autowired private MockMvc mvc;

  @SneakyThrows
  @Test
  void csrfTokenInfo() {
    mvc.perform(get(CSRF_TOKEN_INFO_PATH))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cookieName").value("XSRF-TOKEN"))
        .andExpect(jsonPath("$.headerName").value("X-XSRF-TOKEN"));
  }
}
