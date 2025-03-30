package lv.gennadyyonov.hellookta.api.client.common;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.ACCEPT_HEADER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.APPLICATION_JSON;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.CONTENT_TYPE_HEADER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.readResponse;
import static lv.gennadyyonov.hellookta.api.client.utils.ResponseUtils.extractValueByPattern;
import static lv.gennadyyonov.hellookta.api.client.utils.ResponseUtils.jsonStringAttributeValueRegex;

public class SessionTokenResponseClient {

  private static final Pattern SESSION_TOKEN_PATTERN =
      compile(jsonStringAttributeValueRegex("sessionToken"));

  @SneakyThrows
  public String getTokenResponse(SessionTokenRequest request) {
    URLConnection connection = doPost(request);
    String response = readResponse(connection);
    return extractValueByPattern(SESSION_TOKEN_PATTERN, response);
  }

  @SneakyThrows
  private URLConnection doPost(SessionTokenRequest request) {
    String content = createContent(request);
    Map<String, String> headers = new HashMap<>();
    headers.put(CONTENT_TYPE_HEADER, APPLICATION_JSON);
    headers.put(ACCEPT_HEADER, APPLICATION_JSON);
    return HttpClientUtils.doPost(request.getOrgUrl() + "/api/v1/authn", headers, content);
  }

  private String createContent(SessionTokenRequest request) {
    Map<String, String> params = new LinkedHashMap<>();
    params.put(quoted("username"), quoted(request.getUsername()));
    params.put(quoted("password"), quoted(request.getPassword()));
    AuthOptions options = request.getOptions();
    if (options != null) {
      Map<String, String> optionsParams = new LinkedHashMap<>();
      optionsParams.put(
          quoted("multiOptionalFactorEnroll"), valueOf(options.isMultiOptionalFactorEnroll()));
      optionsParams.put(
          quoted("warnBeforePasswordExpired"), valueOf(options.isWarnBeforePasswordExpired()));
      params.put(quoted("options"), toJson(optionsParams));
    }
    return toJson(params);
  }

  private String quoted(String value) {
    return "\"" + value + "\"";
  }

  private String toJson(Map<String, String> params) {
    String json =
        params.entrySet().stream()
            .map(entry -> format("%s: %s", entry.getKey(), entry.getValue()))
            .collect(joining(", "));
    return "{" + json + "}";
  }
}
