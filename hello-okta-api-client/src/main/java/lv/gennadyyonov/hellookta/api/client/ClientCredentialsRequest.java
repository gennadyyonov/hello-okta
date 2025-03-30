package lv.gennadyyonov.hellookta.api.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientCredentialsRequest {

  private String tokenUri;
  private String clientId;
  private String clientSecret;
  private String scope;
}
