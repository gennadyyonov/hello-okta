package lv.gennadyyonov.hellookta.configuration.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lv.gennadyyonov.hellookta.services.AuthenticationService;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

public class SsoInterceptor implements RequestInterceptor {

    private final AuthenticationService authenticationService;

    public SsoInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void apply(RequestTemplate template) {
        String tokenValue = authenticationService.bearerTokenValue();
        template.header(AUTHORIZATION, format("%s %s", BEARER.getValue(), tokenValue));
    }
}
