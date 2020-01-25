package lv.gennadyyonov.hellookta.bff.connectors.hellooktaapi;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.config.HelloOctaClientProperties;
import lv.gennadyyonov.hellookta.common.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class HelloOktaApiGateway {

    private final HelloOctaClientProperties helloOctaClientProperties;
    private final HelloOktaApiConnector helloOktaApiConnector;
    private final HelloOktaApiConnector runAsHelloOktaApiConnector;

    @Autowired
    public HelloOktaApiGateway(HelloOctaClientProperties helloOctaClientProperties,
                               @Qualifier("helloOktaApiConnector") HelloOktaApiConnector helloOktaApiConnector,
                               @Qualifier("runAsHelloOktaApiConnector") HelloOktaApiConnector runAsHelloOktaApiConnector) {
        this.helloOctaClientProperties = helloOctaClientProperties;
        this.helloOktaApiConnector = helloOktaApiConnector;
        this.runAsHelloOktaApiConnector = runAsHelloOktaApiConnector;
    }

    @SneakyThrows
    public Message helloUser() {
        return sayHello(helloOktaApiConnector);
    }

    @SneakyThrows
    public Message helloClient() {
        return sayHello(runAsHelloOktaApiConnector);
    }

    private Message sayHello(HelloOktaApiConnector connector) throws URISyntaxException {
        URI baseUri = new URI(helloOctaClientProperties.getBaseUrl());
        return connector.hello(baseUri);
    }
}
