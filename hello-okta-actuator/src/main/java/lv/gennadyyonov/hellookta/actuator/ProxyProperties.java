package lv.gennadyyonov.hellookta.actuator;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "management.endpoints.proxy")
@Validated
@RequiredArgsConstructor
@Value
public class ProxyProperties {

  Boolean enabled;
  String path;
  String url;
}
