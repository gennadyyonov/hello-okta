package lv.gennadyyonov.hellookta.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.nio.charset.Charset;

import static org.springframework.util.StreamUtils.copyToString;

@UtilityClass
public class JsonTestUtils {

  private static final ThreadLocal<ObjectMapper> OBJECT_MAPPER =
      ThreadLocal.withInitial(
          () -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.deactivateDefaultTyping();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper;
          });

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
