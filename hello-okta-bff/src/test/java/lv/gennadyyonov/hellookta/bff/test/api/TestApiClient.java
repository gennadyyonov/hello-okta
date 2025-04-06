package lv.gennadyyonov.hellookta.bff.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.config.CsrfTokenContext;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static lv.gennadyyonov.hellookta.bff.controller.CsrfTokenInfoController.CSRF_TOKEN_INFO_PATH;
import static lv.gennadyyonov.hellookta.bff.controller.EnvironmentConfigController.ENVIRONMENT_CONFIG_PATH;
import static lv.gennadyyonov.hellookta.bff.controller.TranslationController.TRANSLATION_MAP_PATH;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SuppressWarnings("SameParameterValue")
@RequiredArgsConstructor
@Component
public class TestApiClient {

  private final MockMvc mvc;
  private final ObjectMapper objectMapper;
  private final CsrfTokenContext csrfTokenContext;

  public TestApiResult getTranslationMap() {
    return doGet(TRANSLATION_MAP_PATH);
  }

  public TestApiResult getEnvironmentConfig() {
    return doGet(ENVIRONMENT_CONFIG_PATH);
  }

  @SneakyThrows
  public TestApiResult getCsrfTokenInfo() {
    var response = mvc.perform(get(CSRF_TOKEN_INFO_PATH)).andDo(print()).andReturn().getResponse();
    return new TestApiResult(response);
  }

  public TestApiResult doGet(String url) {
    return doGet(url, MediaType.APPLICATION_JSON, Map.of());
  }

  private TestApiResult doGet(String path, MediaType contentType, Map<String, String> queryParams) {
    var url = getRestApiPath(path, queryParams);
    return doGet(url, contentType);
  }

  @SneakyThrows
  private TestApiResult doGet(String url, MediaType mediaType) {
    return performRequest(get(url), mediaType);
  }

  @SneakyThrows
  public TestApiResult executeGraphqlQuery(Resource resource) {
    String content = objectMapper.writeValueAsString(resource.getContentAsString(UTF_8));
    String query = String.format("{\"query\": %s}", content);
    return performRequest(post("/graphql").content(query), MediaType.APPLICATION_JSON);
  }

  private TestApiResult performRequest(
      MockHttpServletRequestBuilder requestBuilder, MediaType mediaType) throws Exception {
    requestBuilder.contentType(mediaType).accept(mediaType).characterEncoding(UTF_8);
    var csrfTokenRef = csrfTokenContext.getCsrfTokenRef();
    ofNullable(csrfTokenRef.get())
        .ifPresent(
            token -> {
              requestBuilder.header(token.getHeaderName(), token.getHeaderValue());
              requestBuilder.cookie(new Cookie(token.getCookieName(), token.getCookieValue()));
            });
    var response = mvc.perform(requestBuilder).andDo(print()).andReturn().getResponse();
    return new TestApiResult(response);
  }

  private static String getRestApiPath(String path, Map<String, String> queryParams) {
    var query =
        queryParams.entrySet().stream()
            .map(entry -> format("%s=%s", entry.getKey(), entry.getValue()))
            .collect(Collectors.joining("&"));
    return format("%s%s", path, isBlank(query) ? "" : "?" + query);
  }
}
