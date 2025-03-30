package lv.gennadyyonov.hellookta.api.controller;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.api.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.test.user.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DefaultIntegrationTest
class MessageControllerTest {

  @Autowired private MockMvc mvc;

  @UserInfo("jane.smith@gmail.com")
  @SneakyThrows
  @Test
  void hello() {
    mvc.perform(post("/hello").with(csrf().asHeader()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.text").value("Hello, JANE.SMITH@GMAIL.COM!"));
  }
}
