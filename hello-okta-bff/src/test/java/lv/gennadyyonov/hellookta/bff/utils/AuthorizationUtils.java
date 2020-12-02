package lv.gennadyyonov.hellookta.bff.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.jwt.Jwt;

import static java.util.Collections.singletonList;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;
import static org.springframework.security.oauth2.jwt.Jwt.withTokenValue;

@UtilityClass
public class AuthorizationUtils {

    private static final String DEFAULT_TOKEN_VALUE = "token";

    public static Jwt defaultJwt() {
        return withTokenValue(DEFAULT_TOKEN_VALUE)
                .header("alg", "none")
                .claim("sub", "JOHN.DOE@GMAIL.COM")
                .claim("groups", singletonList("HelloOkta_StandardUser"))
                .build();
    }

    public static String defaultAuthorizationHeader() {
        return BEARER.getValue() + " " + DEFAULT_TOKEN_VALUE;
    }
}
