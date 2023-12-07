package lv.gennadyyonov.hellookta.bff.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.TemplateEngine;
import lv.gennadyyonov.hellookta.bff.test.chucknorris.ChuckNorris;
import lv.gennadyyonov.hellookta.bff.test.hellooktaapi.HelloOktaApi;
import lv.gennadyyonov.hellookta.bff.test.okta.Okta;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

import java.util.List;

public class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        WireMockServer helloOktaApiServer = initWireMockServer(context, HelloOktaApi.SERVER_NAME);
        WireMockServer chuckNorrisServer = initWireMockServer(context, ChuckNorris.SERVER_NAME);
        WireMockServer oktaServer = initWireMockServer(context, Okta.SERVER_NAME);

        context.addApplicationListener(applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                helloOktaApiServer.stop();
                chuckNorrisServer.stop();
                oktaServer.stop();
            }
        });

        TestPropertyValues
            .of(
                "hello-okta-api.url:" + serverUrl(helloOktaApiServer) + "/hello-okta-api",
                "chuck-norris.url:" + serverUrl(chuckNorrisServer) + "/chuck-norris",
                "spring.security.oauth2.client.provider.okta.issuer-uri:" + serverUrl(oktaServer) + "/okta/oauth2/default",
                "cors.allowed-origins:" + serverUrl(oktaServer)
            )
            .applyTo(context);
    }

    private WireMockServer initWireMockServer(ConfigurableApplicationContext context, String name) {
        WireMockConfiguration wireMockConfiguration = createWireMockConfiguration();
        WireMockServer server = new WireMockServer(wireMockConfiguration);
        server.start();
        context.getBeanFactory().registerSingleton(name, server);
        return server;
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

    private String serverUrl(WireMockServer server) {
        return "http://localhost:" + server.port();
    }
}
