package lv.gennadyyonov.hellookta.api.client.common;

import lombok.SneakyThrows;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.ACCEPT_HEADER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.APPLICATION_JSON;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.CONTENT_TYPE_HEADER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.doGet;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.readResponse;
import static lv.gennadyyonov.hellookta.api.client.utils.ResponseUtils.extractValueByPattern;
import static lv.gennadyyonov.hellookta.api.client.utils.ResponseUtils.jsonStringAttributeValueRegex;

public class EnvironmentConfigClient {

  private static final Pattern OKTA_CLIENT_ID_PATTERN =
      compile(jsonStringAttributeValueRegex("oktaClientId"));
  private static final Pattern OKTA_ISSUER_PATTERN =
      compile(jsonStringAttributeValueRegex("oktaIssuer"));

  private final String serverBaseUri;

  public EnvironmentConfigClient(String serverBaseUri) {
    this.serverBaseUri = serverBaseUri;
  }

  @SneakyThrows
  public EnvironmentProperties getEnvironmentProperties() {
    String environmentUri = serverBaseUri + "/config/environment";
    Map<String, String> headers = new HashMap<>();
    headers.put(ACCEPT_HEADER, APPLICATION_JSON);
    headers.put(CONTENT_TYPE_HEADER, APPLICATION_JSON);
    HttpURLConnection connection = doGet(environmentUri, headers);
    String response = readResponse(connection);
    String oktaClientId = extractValueByPattern(OKTA_CLIENT_ID_PATTERN, response);
    String oktaIssuer = extractValueByPattern(OKTA_ISSUER_PATTERN, response);
    return EnvironmentProperties.builder()
        .oktaClientId(oktaClientId)
        .oktaIssuer(oktaIssuer)
        .build();
  }
}
