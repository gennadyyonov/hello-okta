package lv.gennadyyonov.hellookta.bff.services;

import lv.gennadyyonov.hellookta.dto.UserInfo;
import lv.gennadyyonov.hellookta.services.SecurityService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    private final SecurityService securityService;

    public UserInfoService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public UserInfo getUserInfo() {
        return securityService.getUserInfo();
    }
}
