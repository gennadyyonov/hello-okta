package lv.gennadyyonov.hellookta.bff.connectors.hellooktaapi;

import feign.Headers;
import feign.RequestLine;
import lv.gennadyyonov.hellookta.common.dto.Message;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@FeignClient(
    name = "runAsHelloOktaApiConnector",
    configuration = RunAsHelloOktaApiConnectorConfig.class)
public interface RunAsHelloOktaApiConnector extends ParameterLogging {

  @RequestLine("POST /hello")
  @Headers({CONTENT_TYPE + ": " + MediaType.TEXT_PLAIN_VALUE})
  Message hello();
}
