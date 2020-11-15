package lv.gennadyyonov.hellookta.api.client.implicit;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.api.client.common.AuthTokenRequest;
import lv.gennadyyonov.hellookta.api.client.common.AuthTokenResponse;
import lv.gennadyyonov.hellookta.api.client.common.EnvironmentProperties;
import lv.gennadyyonov.hellookta.api.client.common.SessionTokenRequest;
import lv.gennadyyonov.hellookta.api.client.common.SessionTokenResponseClient;

import java.net.HttpURLConnection;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.doGet;
import static lv.gennadyyonov.hellookta.api.client.utils.ResponseUtils.extractRefParams;

public class AuthTokenResponseClient {

    @SneakyThrows
    public AuthTokenResponse getTokenResponse(AuthTokenRequest request) {
        SessionTokenResponseClient sessionTokenResponseClient = new SessionTokenResponseClient();
        SessionTokenRequest sessionTokenRequest = SessionTokenRequest.builder()
                .orgUrl(request.getOrgUrl())
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
        String sessionToken = sessionTokenResponseClient.getTokenResponse(sessionTokenRequest);
        String spaUri = request.getSpaUri();
        EnvironmentProperties environmentProperties = request.getEnvironmentProperties();
        String redirectUri = spaUri + "/implicit/callback";
        String uri = authorizationUri(
                environmentProperties.getOktaIssuer(), environmentProperties.getOktaClientId(), redirectUri, sessionToken
        );
        HttpURLConnection connection = doGet(uri);
        int responseCode = connection.getResponseCode();
        if (responseCode != HTTP_MOVED_TEMP) {
            throw new IllegalStateException();
        }
        String location = connection.getHeaderField("Location");
        Map<String, String> refParams = extractRefParams(location);
        String tokenType = refParams.get("token_type");
        String accessToken = refParams.get("access_token");
        return AuthTokenResponse.builder()
                .tokenType(tokenType)
                .accessToken(accessToken)
                .build();
    }

    @SneakyThrows
    private String authorizationUri(String oktaIssuer, String oktaClientId, String redirectUri, String sessionToken) {
        Map<String, String> queryParams = new LinkedHashMap<>();
        queryParams.put("client_id", oktaClientId);
        queryParams.put("response_type", "token");
        queryParams.put("scope", "email+profile+openid");
        queryParams.put("redirect_uri", encode(redirectUri, UTF_8.name()));
        queryParams.put("state", "foo");
        queryParams.put("nonce", "bar");
        queryParams.put("sessionToken", sessionToken);
        String query = queryParams.entrySet().stream()
                .map(entry -> format("%s=%s", entry.getKey(), entry.getValue()))
                .collect(joining("&"));
        return oktaIssuer + "/v1/authorize?" + query;
    }
}
