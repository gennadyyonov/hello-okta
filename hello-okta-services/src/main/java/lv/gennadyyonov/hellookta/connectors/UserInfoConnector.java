package lv.gennadyyonov.hellookta.connectors;

import feign.HeaderMap;
import feign.RequestLine;
import lv.gennadyyonov.hellookta.logging.LoggingExclusion;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Map;

@FeignClient(name = "userInfoConnector", configuration = UserInfoConnectorConfig.class)
public interface UserInfoConnector extends ParameterLogging {

  @RequestLine("GET")
  @LoggingExclusion
  Map<String, Object> getUserInfo(@LoggingExclusion @HeaderMap Map<String, Object> headers);
}
