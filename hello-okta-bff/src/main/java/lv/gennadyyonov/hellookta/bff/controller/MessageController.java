package lv.gennadyyonov.hellookta.bff.controller;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.connectors.hellooktaapi.HelloOktaApiGateway;
import lv.gennadyyonov.hellookta.common.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final HelloOktaApiGateway helloOktaApiGateway;

    @Autowired
    public MessageController(HelloOktaApiGateway helloOktaApiGateway) {
        this.helloOktaApiGateway = helloOktaApiGateway;
    }

    @SneakyThrows
    @GetMapping("/helloUser")
    public Message helloUser() {
        return helloOktaApiGateway.helloUser();
    }

    @SneakyThrows
    @GetMapping("/helloClient")
    public Message helloClient() {
        return helloOktaApiGateway.helloClient();
    }
}
