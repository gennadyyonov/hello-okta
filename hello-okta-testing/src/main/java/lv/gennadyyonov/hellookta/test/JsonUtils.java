package lv.gennadyyonov.hellookta.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.springframework.util.StreamUtils.copyToString;

@UtilityClass
public class JsonUtils {

  private static ThreadLocal<ObjectMapper> deserializationObjectMapper =
      ThreadLocal.withInitial(
          () -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.deactivateDefaultTyping();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper;
          });

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
