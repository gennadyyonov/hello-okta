package lv.gennadyyonov.hellookta.bff.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DefaultIntegrationTest
public abstract class DefaultIntegrationTestBase {

    @Autowired
    private TestRestTemplateConfigurer restTemplateConfigurer;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        restTemplateConfigurer.setUp(restTemplate);
        wireMockServer.stubFor(
                WireMock.get("/okta/oauth2/default/v1/userinfo")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                                .withBodyFile("okta/oauth2/userinfo.json"))
        );
    }

    @AfterEach
    void tearDown() {
        restTemplateConfigurer.reset(restTemplate);
    }
}
