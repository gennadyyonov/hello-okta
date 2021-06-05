package lv.gennadyyonov.hellookta.bff.config;

import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@DefaultIntegrationTest
public abstract class DefaultIntegrationTestBase {

    @Autowired
    private AuthorizationConfigurer authorizationConfigurer;

    @BeforeEach
    void setUp() {
        authorizationConfigurer.setUp();
    }
}
