package lv.gennadyyonov.hellookta.bff.test.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.test.JsonTestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import tools.jackson.core.type.TypeReference;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

@Getter
@SuppressWarnings({"UnusedReturnValue", "unused"})
@RequiredArgsConstructor
public class TestApiResult {

  private final MockHttpServletResponse response;

  public <T> T getBody(Class<T> clazz) {
    String content = getContentAsString();
    return JsonTestUtils.deserialize(content, clazz);
  }

  public <T> T getBody(TypeReference<T> typeReference) {
    String content = getContentAsString();
    return JsonTestUtils.deserialize(content, typeReference);
  }

  @SneakyThrows
  public String getContentAsString() {
    return response.getContentAsString(UTF_8);
  }

  public TestApiResult assertStatusIs(HttpStatus status) {
    assertThat(response.getStatus()).isEqualTo(status.value());
    return this;
  }
}
