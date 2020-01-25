package lv.gennadyyonov.hellookta.bff.connectors.okta;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.gennadyyonov.hellookta.logging.View;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

    @JsonView(View.LoggingView.class)
    private String token_type;
    private String access_token;
    @JsonView(View.LoggingView.class)
    private Integer expires_in;
    @JsonView(View.LoggingView.class)
    private String scope;
}
