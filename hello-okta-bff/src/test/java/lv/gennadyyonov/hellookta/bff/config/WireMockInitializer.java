package lv.gennadyyonov.hellookta.bff.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        WireMockConfiguration wireMockConfiguration = new WireMockConfiguration()
                .dynamicPort()
                .extensions(new ResponseTemplateTransformer(false));
        WireMockServer wireMockServer = new WireMockServer(wireMockConfiguration);
        wireMockServer.start();

        configurableApplicationContext.getBeanFactory().registerSingleton("wireMockServer", wireMockServer);

        configurableApplicationContext.addApplicationListener(applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                wireMockServer.stop();
            }
        });

        String wireMockServerUrl = "http://localhost:" + wireMockServer.port();

        TestPropertyValues
                .of(
                        "hellooktaapiclient_baseUrl:" + wireMockServerUrl + "/hello-okta-api",
                        "issuer:" + wireMockServerUrl + "/okta/oauth2/default"
                )
                .applyTo(configurableApplicationContext);

        wireMockServer.stubFor(
                WireMock.get("/okta/oauth2/default/.well-known/openid-configuration")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                                .withBodyFile("okta/oauth2/well-known-openid-configuration.json")
                                .withTransformers("response-template"))
        );
        wireMockServer.stubFor(
                WireMock.get("/okta/oauth2/default/v1/keys")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                                .withBodyFile("okta/oauth2/keys.json"))
        );
    }
}
