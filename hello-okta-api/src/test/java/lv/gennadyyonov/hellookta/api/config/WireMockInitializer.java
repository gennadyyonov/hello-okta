package lv.gennadyyonov.hellookta.api.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import lv.gennadyyonov.hellookta.api.test.okta.Okta;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

public class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        WireMockConfiguration config = createWireMockConfiguration();
        WireMockServer oktaServer = new WireMockServer(config);
        oktaServer.start();

        context.getBeanFactory().registerSingleton(Okta.SERVER_NAME, oktaServer);

        context.addApplicationListener(applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                oktaServer.stop();
            }
        });

        String oktaServerUrl = "http://localhost:" + oktaServer.port();

        TestPropertyValues
            .of("spring.security.oauth2.client.provider.okta.issuer-uri:" + oktaServerUrl + "/okta/oauth2/default")
            .applyTo(context);
    }

    private WireMockConfiguration createWireMockConfiguration() {
        return new WireMockConfiguration()
            .dynamicPort()
            .usingFilesUnderClasspath("wiremock")
            .extensions(new ResponseTemplateTransformer(true));
    }
}
