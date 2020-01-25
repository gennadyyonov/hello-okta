package lv.gennadyyonov.hellookta.bff.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lv.gennadyyonov.hellookta.bff.connectors.okta.TokenGateway;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

public class ClientCredentialsInterceptor implements RequestInterceptor {

    private final TokenGateway tokenGateway;

    public ClientCredentialsInterceptor(TokenGateway tokenGateway) {
        this.tokenGateway = tokenGateway;
    }

    @Override
    public void apply(RequestTemplate template) {
        String accessToken = tokenGateway.getClientCredentialsAccessToken();
        template.header(AUTHORIZATION, format("%s %s", BEARER.getValue(), accessToken));
    }
}
