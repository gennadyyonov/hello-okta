package lv.gennadyyonov.hellookta.bff.connectors.okta;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.services.SecurityService;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class TokenGateway {

    private final ClientCredentialsResourceDetails clientCredentialsResourceDetails;
    private final TokenConnector tokenConnector;
    private final SecurityService securityService;

    public TokenGateway(ClientCredentialsResourceDetails clientCredentialsResourceDetails,
                        TokenConnector tokenConnector,
                        SecurityService securityService) {
        this.clientCredentialsResourceDetails = clientCredentialsResourceDetails;
        this.tokenConnector = tokenConnector;
        this.securityService = securityService;
    }

    /**
     * @see <a href="https://developer.okta.com/docs/guides/implement-client-creds/use-flow/">Use the Client Credentials Flow</a>
     */
    @SneakyThrows
    public String getClientCredentialsAccessToken() {
        URI baseUri = new URI(clientCredentialsResourceDetails.getAccessTokenUri());
        Map<String, Object> headers = headers();
        String grantType = clientCredentialsResourceDetails.getGrantType();
        String scope = getScope();
        TokenResponse tokenResponse = tokenConnector.getAccessToken(baseUri, headers, grantType, scope);
        return tokenResponse.getAccess_token();
    }

    private Map<String, Object> headers() {
        Map<String, Object> headers = new HashMap<>();
        headers.put(AUTHORIZATION, securityService.basicAuthorizationHeaderValue(clientCredentialsResourceDetails));
        return headers;
    }

    private String getScope() {
        List<String> scope = clientCredentialsResourceDetails.getScope();
        return ofNullable(scope).map(values -> String.join(" ", values)).orElse(null);
    }
}
