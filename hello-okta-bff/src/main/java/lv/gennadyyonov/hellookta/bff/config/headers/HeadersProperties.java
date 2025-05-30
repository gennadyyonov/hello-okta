package lv.gennadyyonov.hellookta.bff.config.headers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "headers")
@Validated
@RequiredArgsConstructor
@Value
public class HeadersProperties {

  @Valid Csp csp;

  @Value
  static class Csp {

    @NotBlank String directives;
  }
}
