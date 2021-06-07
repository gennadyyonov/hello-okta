package lv.gennadyyonov.hellookta.test.user;

import java.util.List;

public interface UserInfoConfig {

    void setUp(String username, List<String> groups);

    void reset();
}
