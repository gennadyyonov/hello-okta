package lv.gennadyyonov.hellookta.bff.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lv.gennadyyonov.hellookta.bff.services.SecurityService;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

@Component
public class SsoInterceptor implements RequestInterceptor {

    private final SecurityService securityService;

    public SsoInterceptor(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void apply(RequestTemplate template) {
        String tokenValue = securityService.bearerTokenValue();
        template.header(AUTHORIZATION, format("%s %s", BEARER.getValue(), tokenValue));
    }
}
