package lv.gennadyyonov.hellookta.bff.config;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static lv.gennadyyonov.hellookta.bff.utils.AuthorizationUtils.defaultJwt;
import static org.mockito.Mockito.doReturn;

@DefaultIntegrationTest
public abstract class DefaultIntegrationTestBase {

    @Autowired
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        Jwt jwt = defaultJwt();
        doReturn(jwt).when(jwtDecoder).decode(jwt.getTokenValue());
    }
}
