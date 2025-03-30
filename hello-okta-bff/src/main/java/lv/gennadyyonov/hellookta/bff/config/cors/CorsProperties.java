package lv.gennadyyonov.hellookta.bff.config.cors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties(prefix = "cors")
@RequiredArgsConstructor
@Validated
@Value
public class CorsProperties {

  @NotBlank String urlPattern;
  Boolean allowCredentials;
  List<String> allowedOrigins;
  @NotNull List<String> allowedHeaders;
  @NotNull List<String> allowedMethods;
}
