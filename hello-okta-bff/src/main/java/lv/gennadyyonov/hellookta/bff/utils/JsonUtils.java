package lv.gennadyyonov.hellookta.bff.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.springframework.util.StreamUtils.copyToString;
import static tools.jackson.databind.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES;
import static tools.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@UtilityClass
public class JsonUtils {

  private static ThreadLocal<ObjectMapper> deserializationObjectMapper =
      ThreadLocal.withInitial(
          () ->
              JsonMapper.builder()
                  .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                  .configure(FAIL_ON_NULL_FOR_PRIMITIVES, false)
                  .deactivateDefaultTyping()
                  .build());

  public static <T> T resourceToObject(String name, TypeReference<T> typeReference) {
    return resourceToObject(name, emptyMap(), typeReference);
  }

  public static <T> T resourceToObject(
      String name, Map<String, Object> replacements, TypeReference<T> typeReference) {
    InputStream inputStream = JsonUtils.class.getResourceAsStream(name);
    return resourceToObject(inputStream, replacements, typeReference);
  }

  public static <T> T resourceToObject(InputStream inputStream, TypeReference<T> typeReference) {
    return resourceToObject(inputStream, emptyMap(), typeReference);
  }

  @SneakyThrows
  private static <T> T resourceToObject(
      InputStream inputStream, Map<String, Object> replacements, TypeReference<T> typeReference) {
    String json = copyToString(inputStream, Charset.defaultCharset());
    json = applyReplacements(json, replacements);
    return deserializationObjectMapper().readValue(json, typeReference);
  }

  private static String applyReplacements(String content, Map<String, Object> replacements) {
    for (Map.Entry<String, Object> entry : replacements.entrySet()) {
      content = content.replace(entry.getKey(), String.valueOf(entry.getValue()));
    }
    return content;
  }

  private static ObjectMapper deserializationObjectMapper() {
    return deserializationObjectMapper.get();
  }
}
