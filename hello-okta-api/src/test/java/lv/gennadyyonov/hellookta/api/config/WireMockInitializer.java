package lv.gennadyyonov.hellookta.api.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

public class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        WireMockConfiguration wireMockConfiguration = new WireMockConfiguration()
                .dynamicPort()
                .usingFilesUnderClasspath("wiremock")
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
                .of("issuer:" + wireMockServerUrl + "/okta/oauth2/default")
                .applyTo(configurableApplicationContext);
    }
}