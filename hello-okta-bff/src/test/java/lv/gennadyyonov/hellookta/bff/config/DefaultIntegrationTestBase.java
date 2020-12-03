package lv.gennadyyonov.hellookta.bff.config;

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
