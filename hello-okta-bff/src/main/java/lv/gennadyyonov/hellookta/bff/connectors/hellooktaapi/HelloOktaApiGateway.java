package lv.gennadyyonov.hellookta.bff.connectors.hellooktaapi;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.common.dto.Message;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelloOktaApiGateway {

    private final HelloOktaApiConnector helloOktaApiConnector;
    private final RunAsHelloOktaApiConnector runAsHelloOktaApiConnector;

    @SneakyThrows
    public Message helloUser() {
        return helloOktaApiConnector.hello();
    }

    @SneakyThrows
    public Message helloClient() {
        return runAsHelloOktaApiConnector.hello();
    }
}
