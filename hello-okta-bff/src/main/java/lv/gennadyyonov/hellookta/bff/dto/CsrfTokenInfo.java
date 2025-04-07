package lv.gennadyyonov.hellookta.bff.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Represents CSRF token configuration details.")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CsrfTokenInfo {

  @Schema(
      description = "The name of the cookie that stores the CSRF token.",
      example = "XSRF-TOKEN")
  private String cookieName;

  @Schema(
      description = "The name of the HTTP header used to send the CSRF token in requests.",
      example = "X-XSRF-TOKEN")
  private String headerName;
}
