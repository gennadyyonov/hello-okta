package lv.gennadyyonov.hellookta.api.controller;

import lv.gennadyyonov.hellookta.api.service.MessageService;
import lv.gennadyyonov.hellookta.common.dto.Message;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController implements ParameterLogging {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/hello")
    public Message hello() {
        return messageService.sayHello();
    }
}
