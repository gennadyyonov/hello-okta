package lv.gennadyyonov.hellookta.api.client.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionTokenRequest {

    private String orgUrl;
    private String username;
    private String password;
    @Builder.Default
    private AuthOptions options = new AuthOptions();
}
