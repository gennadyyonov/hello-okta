package lv.gennadyyonov.hellookta.bff.connectors.hellooktaapi;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.config.HelloOktaApiClientProperties;
import lv.gennadyyonov.hellookta.common.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class HelloOktaApiGateway {

    private final HelloOktaApiClientProperties helloOktaApiClientProperties;
    private final HelloOktaApiConnector helloOktaApiConnector;
    private final RunAsHelloOktaApiConnector runAsHelloOktaApiConnector;

    @Autowired
    public HelloOktaApiGateway(HelloOktaApiClientProperties helloOktaApiClientProperties,
                               HelloOktaApiConnector helloOktaApiConnector,
                               RunAsHelloOktaApiConnector runAsHelloOktaApiConnector) {
        this.helloOktaApiClientProperties = helloOktaApiClientProperties;
        this.helloOktaApiConnector = helloOktaApiConnector;
        this.runAsHelloOktaApiConnector = runAsHelloOktaApiConnector;
    }

    @SneakyThrows
    public Message helloUser() {
        URI baseUri = new URI(helloOktaApiClientProperties.getBaseUrl());
        return helloOktaApiConnector.hello(baseUri);
    }

    @SneakyThrows
    public Message helloClient() {
        URI baseUri = new URI(helloOktaApiClientProperties.getBaseUrl());
        return runAsHelloOktaApiConnector.hello(baseUri);
    }
}
