package lv.gennadyyonov.hellookta.bff.test.user;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.bff.test.okta.Okta;
import lv.gennadyyonov.hellookta.test.user.UserInfoConfig;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Component
public class OktaUserInfoConfig implements UserInfoConfig {

    private final Okta okta;

    @Override
    public void setUp(String username, List<String> groups) {
        okta.stubFor(
            WireMock.get("/okta/oauth2/default/v1/userinfo")
                .willReturn(aResponse()
                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                    .withBodyFile("okta/oauth2/" + username.toLowerCase() + ".json"))
        );
    }

    @Override
    public void reset() {
    }
}
