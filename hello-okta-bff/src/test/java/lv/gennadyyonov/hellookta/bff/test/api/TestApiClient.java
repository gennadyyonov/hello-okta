package lv.gennadyyonov.hellookta.bff.test.api;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static lv.gennadyyonov.hellookta.bff.controller.TranslationController.TRANSLATION_MAP_PATH;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SuppressWarnings("SameParameterValue")
@RequiredArgsConstructor
@Component
public class TestApiClient {

  private final MockMvc mvc;

  public TestApiResult getTranslationMap() {
    return doGet(TRANSLATION_MAP_PATH);
  }

  private TestApiResult doGet(String url) {
    return doGet(url, MediaType.APPLICATION_JSON, Map.of());
  }

  private TestApiResult doGet(String path, MediaType contentType, Map<String, String> queryParams) {
    var url = getRestApiPath(path, queryParams);
    return doGetFromUrl(url, contentType);
  }

  @SneakyThrows
  private TestApiResult doGetFromUrl(String url, MediaType contentType) {
    var requestBuilder = get(url).contentType(contentType).characterEncoding(UTF_8);
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
