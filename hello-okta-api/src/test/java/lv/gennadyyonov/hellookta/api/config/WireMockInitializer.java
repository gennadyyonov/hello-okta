package lv.gennadyyonov.hellookta.api.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.TemplateEngine;
import lv.gennadyyonov.hellookta.api.test.okta.Okta;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

import java.util.List;

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
        FileSource fileSource = new ClasspathFileSource("src/test/resources/wiremock");
        return new WireMockConfiguration()
            .dynamicPort()
            .usingFilesUnderClasspath("wiremock")
            .extensions(new ResponseTemplateTransformer(
                TemplateEngine.defaultTemplateEngine(),
                true,
                fileSource,
                List.of()
            ));
    }
}
