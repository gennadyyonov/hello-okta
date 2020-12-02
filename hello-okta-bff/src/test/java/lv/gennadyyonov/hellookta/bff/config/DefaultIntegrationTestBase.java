package lv.gennadyyonov.hellookta.bff.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DefaultIntegrationTest
public abstract class DefaultIntegrationTestBase {

    @Autowired
    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        wireMockServer.stubFor(
                WireMock.get("/okta/oauth2/default/v1/userinfo")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                                .withBodyFile("okta/oauth2/userinfo.json"))
        );
    }
}
