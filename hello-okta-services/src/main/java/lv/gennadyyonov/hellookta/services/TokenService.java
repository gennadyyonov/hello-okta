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

    private final RunAsDetails runAsDetails;
    private final TokenConnector tokenConnector;
    private final AuthenticationService authenticationService;

    public TokenService(RunAsDetails runAsDetails,
                        TokenConnector tokenConnector,
                        AuthenticationService authenticationService) {
        this.runAsDetails = runAsDetails;
        this.tokenConnector = tokenConnector;
        this.authenticationService = authenticationService;
    }

    /**
     * @see <a href="https://developer.okta.com/docs/guides/implement-client-creds/use-flow/">Use the Client Credentials Flow</a>
     */
    @SneakyThrows
    public String getClientCredentialsAccessToken() {
        URI baseUri = new URI(runAsDetails.getAccessTokenUri());
        Map<String, Object> headers = headers();
        String grantType = runAsDetails.getGrantType();
        String scope = getScope();
        TokenResponse tokenResponse = tokenConnector.getAccessToken(baseUri, headers, grantType, scope);
        return tokenResponse.getAccess_token();
    }

    private Map<String, Object> headers() {
        Map<String, Object> headers = new HashMap<>();
        headers.put(
                AUTHORIZATION,
                authenticationService.basicAuthorizationHeaderValue(runAsDetails.getClientId(), runAsDetails.getClientSecret())
        );
        return headers;
    }

    private String getScope() {
        List<String> scope = runAsDetails.getScope();
        return ofNullable(scope).map(values -> String.join(" ", values)).orElse(null);
    }
}
