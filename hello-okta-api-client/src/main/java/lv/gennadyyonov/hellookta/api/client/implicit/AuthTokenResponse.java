package lv.gennadyyonov.hellookta.api.client.implicit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthTokenResponse {

    private String tokenType;
    private String accessToken;
}
