package lv.gennadyyonov.hellookta.bff.dto;

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
  private Boolean csrfEnabled;
}
