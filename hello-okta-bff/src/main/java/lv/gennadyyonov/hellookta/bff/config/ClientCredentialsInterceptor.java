package lv.gennadyyonov.hellookta.bff.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lv.gennadyyonov.hellookta.services.TokenService;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

public class ClientCredentialsInterceptor implements RequestInterceptor {

    private final TokenService tokenService;

    public ClientCredentialsInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void apply(RequestTemplate template) {
        String accessToken = tokenService.getClientCredentialsAccessToken();
        template.header(AUTHORIZATION, format("%s %s", BEARER.getValue(), accessToken));
    }
}
