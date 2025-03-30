package lv.gennadyyonov.hellookta.api.client.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthTokenRequest {

  private String spaUri;
  private EnvironmentProperties environmentProperties;
  private String orgUrl;
  private String username;
  private String password;
}
