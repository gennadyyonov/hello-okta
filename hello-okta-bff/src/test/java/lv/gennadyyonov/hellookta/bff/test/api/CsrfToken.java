package lv.gennadyyonov.hellookta.bff.test.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CsrfToken {

  private String headerName;
  private String cookieName;
  private String headerValue;
  private String cookieValue;
}
