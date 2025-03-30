package lv.gennadyyonov.hellookta.connectors;

import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import lv.gennadyyonov.hellookta.dto.TokenResponse;
import lv.gennadyyonov.hellookta.logging.LoggingExclusion;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@FeignClient(name = "tokenConnector", configuration = TokenConnectorConfig.class)
public interface TokenConnector extends ParameterLogging {

  @RequestLine("POST")
  @Headers({
    CONTENT_TYPE + ": " + MediaType.APPLICATION_FORM_URLENCODED_VALUE,
    ACCEPT + ": " + MediaType.APPLICATION_JSON_VALUE,
    CACHE_CONTROL + ": no-cache"
  })
  TokenResponse getAccessToken(
      @LoggingExclusion @HeaderMap Map<String, Object> headers,
      @Param("grant_type") String grantType,
      @Param("scope") String scope);
}
