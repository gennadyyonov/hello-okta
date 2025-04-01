package lv.gennadyyonov.hellookta.bff.config;

import lombok.Getter;
import lv.gennadyyonov.hellookta.test.user.UserInfoConfig;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Component
public class TokenInfoContext implements UserInfoConfig {

  private final AtomicReference<TokenInfo> tokenInfoRef = new AtomicReference<>(null);

  @Override
  public void setUp(String username, List<String> groups) {
    tokenInfoRef.set(new TokenInfo(username, groups));
  }

  @Override
  public void reset() {
    tokenInfoRef.set(null);
  }
}
