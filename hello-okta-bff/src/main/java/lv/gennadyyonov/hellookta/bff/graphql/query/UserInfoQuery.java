package lv.gennadyyonov.hellookta.bff.graphql.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lv.gennadyyonov.hellookta.aspects.HasRole;
import lv.gennadyyonov.hellookta.bff.services.UserInfoService;
import lv.gennadyyonov.hellookta.dto.UserInfo;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import org.springframework.stereotype.Component;

import static lv.gennadyyonov.hellookta.constants.SecurityConstants.ALLOWED_USERS;

@HasRole(ALLOWED_USERS)
@Component
public class UserInfoQuery implements GraphQLQueryResolver, ParameterLogging {

    private final UserInfoService userInfoService;

    public UserInfoQuery(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    public UserInfo me() {
        return userInfoService.getUserInfo();
    }
}
