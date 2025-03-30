package lv.gennadyyonov.hellookta.test.http;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.DelayDistribution;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import lombok.RequiredArgsConstructor;
import wiremock.com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

@RequiredArgsConstructor
public class Response {

  private final Server.Stubbing stubbing;
  private final MappingBuilder request;
  private final ResponseDefinitionBuilder response;

  public Response status(int status) {
    response.withStatus(status);
    return this;
  }

  public Response header(String key, String... values) {
    response.withHeader(key, values);
    return this;
  }

  public Response bodyFile(String fileName) {
    response.withBodyFile(fileName);
    return this;
  }

  public Response body(String body) {
    response.withBody(body);
    return this;
  }

  public Response body(byte[] body) {
    response.withBody(body);
    return this;
  }

  public Response jsonBody(JsonNode jsonBody) {
    response.withJsonBody(jsonBody);
    return this;
  }

  public Response fixedDelay(Integer milliseconds) {
    response.withFixedDelay(milliseconds);
    return this;
  }

  public Response randomDelay(DelayDistribution distribution) {
    response.withRandomDelay(distribution);
    return this;
  }

  public Response logNormalRandomDelay(double medianMilliseconds, double sigma) {
    response.withLogNormalRandomDelay(medianMilliseconds, sigma);
    return this;
  }

  public Response uniformRandomDelay(int lowerMilliseconds, int upperMilliseconds) {
    response.withUniformRandomDelay(lowerMilliseconds, upperMilliseconds);
    return this;
  }

  public Response chunkedDribbleDelay(int numberOfChunks, int totalDuration) {
    response.withChunkedDribbleDelay(numberOfChunks, totalDuration);
    return this;
  }

  public Response transformers(String... responseTransformerNames) {
    response.withTransformers(responseTransformerNames);
    return this;
  }

  public Response transformerParameters(Map<String, Object> parameters) {
    response.withTransformerParameters(parameters);
    return this;
  }

  public Response transformerParameter(String name, Object value) {
    response.withTransformerParameter(name, value);
    return this;
  }

  public Response transformer(String transformerName, String parameterKey, Object parameterValue) {
    response.withTransformer(transformerName, parameterKey, parameterValue);
    return this;
  }

  public Response headers(HttpHeaders headers) {
    response.withHeaders(headers);
    return this;
  }

  public Response base64Body(String base64Body) {
    response.withBase64Body(base64Body);
    return this;
  }

  public Response statusMessage(String message) {
    response.withStatusMessage(message);
    return this;
  }

  public Response fault(Fault fault) {
    response.withFault(fault);
    return this;
  }

  public void endStubbing() {
    stubbing.accept(request, response);
  }
}
