package lv.gennadyyonov.hellookta.bff.test.user;

import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.bff.test.okta.Okta;
import lv.gennadyyonov.hellookta.test.user.UserInfoConfig;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Component
public class OktaUserInfoConfig implements UserInfoConfig {

  private final Okta okta;

  @Override
  public void setUp(String username, List<String> groups) {
    okta.onGetUserInfo()
        .expect()
        .header("Content-Type", APPLICATION_JSON_VALUE)
        .bodyFile("okta/oauth2/" + username.toLowerCase() + ".json")
        .endStubbing();
  }

  @Override
  public void reset() {}
}
