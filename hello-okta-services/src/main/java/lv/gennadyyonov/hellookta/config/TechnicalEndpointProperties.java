package lv.gennadyyonov.hellookta.config;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties(prefix = "technical-endpoint")
@Validated
@RequiredArgsConstructor
@Value
public class TechnicalEndpointProperties {

  @NotNull Boolean enabled;
  List<String> additionalRealEndpoints;
  List<String> referrerHeaderNames;
  @NotNull List<Endpoint> endpoints;

  @Validated
  @Value
  public static class Endpoint {

    @NotNull Boolean enabled;
    List<String> allowedClasses;
    List<String> allowedEndpoints;
  }
}
