package lv.gennadyyonov.hellookta.bff.graphql.query;

import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.aspects.HasRole;
import lv.gennadyyonov.hellookta.dto.UserInfo;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import lv.gennadyyonov.hellookta.logging.PerformanceLogging;
import lv.gennadyyonov.hellookta.services.UserInfoService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import static lv.gennadyyonov.hellookta.bff.config.Constants.GRAPHQL;
import static lv.gennadyyonov.hellookta.constants.SecurityConstants.ALLOWED_USERS;

@PerformanceLogging(GRAPHQL)
@HasRole(ALLOWED_USERS)
@Controller
@RequiredArgsConstructor
public class UserInfoQuery implements ParameterLogging {

  private final UserInfoService userInfoService;

  @QueryMapping
  public UserInfo me() {
    return userInfoService.getUserInfo();
  }
}
