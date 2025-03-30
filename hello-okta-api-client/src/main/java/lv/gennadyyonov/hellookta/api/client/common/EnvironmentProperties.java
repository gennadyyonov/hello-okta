package lv.gennadyyonov.hellookta.api.client.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EnvironmentProperties {

  private String oktaClientId;
  private String oktaIssuer;
}
