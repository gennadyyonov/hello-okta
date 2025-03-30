package lv.gennadyyonov.hellookta.api.client.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthOptions {

  private boolean multiOptionalFactorEnroll;
  private boolean warnBeforePasswordExpired;
}
