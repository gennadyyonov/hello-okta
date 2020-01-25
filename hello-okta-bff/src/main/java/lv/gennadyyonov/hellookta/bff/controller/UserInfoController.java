package lv.gennadyyonov.hellookta.bff.controller;

import lv.gennadyyonov.hellookta.bff.dto.UserInfo;
import lv.gennadyyonov.hellookta.bff.services.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
