package lv.gennadyyonov.hellookta.api.service;

import lv.gennadyyonov.hellookta.common.dto.Message;
import lv.gennadyyonov.hellookta.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class MessageService {

    private static final String GREETING_FORMAT = "Hello, %s!";

    private final SecurityService securityService;

    @Autowired
    public MessageService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public Message sayHello() {
        String userId = securityService.getUserId();
        return Message.builder()
                .text(format(GREETING_FORMAT, userId))
                .build();
    }
}
