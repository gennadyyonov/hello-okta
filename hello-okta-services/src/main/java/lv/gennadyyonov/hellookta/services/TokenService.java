package lv.gennadyyonov.hellookta.services;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.connectors.TokenConnector;
import lv.gennadyyonov.hellookta.dto.RunAsDetails;
import lv.gennadyyonov.hellookta.dto.TokenResponse;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class TokenService {

    private final TokenConnector tokenConnector;
    private final AuthenticationService authenticationService;

    public TokenService(TokenConnector tokenConnector,
                        AuthenticationService authenticationService) {
        this.tokenConnector = tokenConnector;
        this.authenticationService = authenticationService;
    }

    /**
     * Gets access token for Client Credentials flow.
     *
     * @see <a href="https://developer.okta.com/docs/guides/implement-client-creds/use-flow/">Use the Client Credentials Flow</a>
     */
    @SneakyThrows
    public String getClientCredentialsAccessToken(RunAsDetails runAsDetails) {
        URI baseUri = new URI(runAsDetails.getAccessTokenUri());
        Map<String, Object> headers = headers(runAsDetails);
        String grantType = runAsDetails.getGrantType();
        String scope = getScope(runAsDetails);
        TokenResponse tokenResponse = tokenConnector.getAccessToken(baseUri, headers, grantType, scope);
        return tokenResponse.getAccessToken();
    }

    private Map<String, Object> headers(RunAsDetails runAsDetails) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(
            AUTHORIZATION,
            authenticationService.basicAuthorizationHeaderValue(runAsDetails.getClientId(), runAsDetails.getClientSecret())
        );
        return headers;
    }

    private String getScope(RunAsDetails runAsDetails) {
        List<String> scope = runAsDetails.getScope();
        return ofNullable(scope).map(values -> String.join(" ", values)).orElse(null);
    }
}
