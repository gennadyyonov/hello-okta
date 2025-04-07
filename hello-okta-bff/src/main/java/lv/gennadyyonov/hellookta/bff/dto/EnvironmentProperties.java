package lv.gennadyyonov.hellookta.bff.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Holds environment configuration properties.")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EnvironmentProperties {

  @Schema(
      description = "The unique identifier for the Okta client application.",
      example = "0oa1abc23DEF456ghi7")
  private String oktaClientId;

  @Schema(
      description = "The issuer URI of the Okta authorization server used for authentication.",
      example = "https://your-domain.okta.com/oauth2/default")
  private String oktaIssuer;

  @Schema(description = "Indicates whether CSRF protection is enabled.", example = "true")
  private Boolean csrfEnabled;
}
