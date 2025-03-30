package lv.gennadyyonov.hellookta.api.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ClientConfig {

  private String issuer;
  private String clientId;
  private String clientSecret;
  private String scope;
  private String serverUri;
}
