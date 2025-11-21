package lv.gennadyyonov.hellookta.test;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;
import java.nio.charset.Charset;

import static org.springframework.util.StreamUtils.copyToString;
import static tools.jackson.databind.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES;
import static tools.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@UtilityClass
public class JsonTestUtils {

  private static final ThreadLocal<ObjectMapper> OBJECT_MAPPER =
      ThreadLocal.withInitial(
          () ->
              JsonMapper.builder()
                  .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                  .configure(FAIL_ON_NULL_FOR_PRIMITIVES, false)
                  .deactivateDefaultTyping()
                  .build());

  @SneakyThrows
  public static <T> T deserialize(InputStream inputStream, TypeReference<T> typeReference) {
    String json = copyToString(inputStream, Charset.defaultCharset());
    return deserialize(json, typeReference);
  }

  @SneakyThrows
  public static <T> T deserialize(String json, Class<T> clazz) {
    return objectMapper().readValue(json, clazz);
  }

  @SneakyThrows
  public static <T> T deserialize(String json, TypeReference<T> typeReference) {
    return objectMapper().readValue(json, typeReference);
  }

  @SneakyThrows
  public static <T> String serialize(T data) {
    return objectMapper().writeValueAsString(data);
  }

  private static ObjectMapper objectMapper() {
    return OBJECT_MAPPER.get();
  }
}
