package lv.gennadyyonov.hellookta.api.client.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.URLDecoder.decode;
import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
public class ResponseUtils {

  public static String jsonStringAttributeValueRegex(String attribute) {
    return ".*\"" + attribute + "\"\\s*:\\s*\"([^\"]+)\".*";
  }

  public static String jsonAttributeValueRegex(String attribute) {
    return ".*\"" + attribute + "\"\\s*:\\s*([^\",]+).*(,.*)";
  }

  public static String extractValueByPattern(Pattern pattern, String response) {
    Matcher matcher = pattern.matcher(response);
    if (matcher.matches() && matcher.groupCount() > 0) {
      return matcher.group(1);
    }
    return null;
  }

  @SneakyThrows
  public static Map<String, String> extractRefParams(String uri) {
    URL url = new URL(uri);
    Map<String, String> nameValuePairs = new HashMap<>();
    String query = url.getRef();
    String[] pairs = query.split("&");
    for (String pair : pairs) {
      int idx = pair.indexOf('=');
      String name = decode(pair.substring(0, idx), UTF_8.name());
      String value = decode(pair.substring(idx + 1), UTF_8.name());
      nameValuePairs.put(name, value);
    }
    return nameValuePairs;
  }
}
