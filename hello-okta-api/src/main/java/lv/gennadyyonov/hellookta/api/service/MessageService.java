package lv.gennadyyonov.hellookta.api.service;

import lv.gennadyyonov.hellookta.common.dto.Message;
import lv.gennadyyonov.hellookta.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class MessageService {

    private static final String GREETING_FORMAT = "Hello, %s!";

    private final AuthenticationService authenticationService;

    @Autowired
    public MessageService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public Message sayHello() {
        String userId = authenticationService.getUserId();
        return Message.builder()
                .text(format(GREETING_FORMAT, userId))
                .build();
    }
}
