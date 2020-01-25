package lv.gennadyyonov.hellookta.bff.connectors.okta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

    private String token_type;
    private String access_token;
    private Integer expires_in;
    private String scope;
}
