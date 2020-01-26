package lv.gennadyyonov.hellookta.bff.controller;

import lv.gennadyyonov.hellookta.aspects.HasRole;
import lv.gennadyyonov.hellookta.bff.services.UserInfoService;
import lv.gennadyyonov.hellookta.constants.SecurityConstants;
import lv.gennadyyonov.hellookta.dto.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static lv.gennadyyonov.hellookta.constants.SecurityConstants.ALLOWED_USERS;

@HasRole(ALLOWED_USERS)
@RestController
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/me")
    public UserInfo me() {
        return userInfoService.getUserInfo();
    }
}
