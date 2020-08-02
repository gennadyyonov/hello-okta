package lv.gennadyyonov.hellookta.api.client.pkce;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.api.client.common.AuthTokenRequest;
import lv.gennadyyonov.hellookta.api.client.common.AuthTokenResponse;
import lv.gennadyyonov.hellookta.api.client.common.EnvironmentProperties;
import lv.gennadyyonov.hellookta.api.client.common.SessionTokenRequest;
import lv.gennadyyonov.hellookta.api.client.common.SessionTokenResponseClient;
import lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils;

import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static lv.gennadyyonov.hellookta.api.client.pkce.PKCEGenerator.CODE_CHALLENGE;
import static lv.gennadyyonov.hellookta.api.client.pkce.PKCEGenerator.CODE_CHALLENGE_METHOD;
import static lv.gennadyyonov.hellookta.api.client.pkce.PKCEGenerator.CODE_VERIFIER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.ACCEPT_HEADER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.APPLICATION_JSON;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.APPLICATION_X_WWW_FORM_URLENCODED;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.CONTENT_TYPE_HEADER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.doGet;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.readResponse;
import static lv.gennadyyonov.hellookta.api.client.utils.ResponseUtils.extractRefParams;
import static lv.gennadyyonov.hellookta.api.client.utils.ResponseUtils.extractValueByPattern;
import static lv.gennadyyonov.hellookta.api.client.utils.ResponseUtils.jsonStringAttributeValueRegex;

public class PKCEAuthTokenResponseClient {

    private static final String TOKEN_PATH = "/v1/token";
    private static final Pattern ACCESS_TOKEN_PATTERN = compile(jsonStringAttributeValueRegex("access_token"));
    private static final Pattern TOKEN_TYPE_PATTERN = compile(jsonStringAttributeValueRegex("token_type"));

    @SneakyThrows
    public AuthTokenResponse getTokenResponse(AuthTokenRequest request) {
        SessionTokenResponseClient sessionTokenResponseClient = new SessionTokenResponseClient();
        SessionTokenRequest sessionTokenRequest = SessionTokenRequest.builder()
                .orgUrl(request.getOrgUrl())
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
        String sessionToken = sessionTokenResponseClient.getTokenResponse(sessionTokenRequest);
        PKCEGenerator pkceGenerator = new PKCEGenerator();
        Map<String, String> pkceParameters = pkceGenerator.pkceParameters();
        String spaUri = request.getSpaUri();
        EnvironmentProperties environmentProperties = request.getEnvironmentProperties();
        String redirectUri = spaUri + "/implicit/callback";
        String oktaIssuer = environmentProperties.getOktaIssuer();
        String oktaClientId = environmentProperties.getOktaClientId();
        String uri = authorizationUri(oktaIssuer, oktaClientId, redirectUri, sessionToken, pkceParameters);
        HttpURLConnection connection = doGet(uri);
        int responseCode = connection.getResponseCode();
        if (responseCode != HTTP_MOVED_TEMP) {
            throw new IllegalStateException();
        }
        String location = connection.getHeaderField("Location");
        Map<String, String> refParams = extractRefParams(location);
        String code = refParams.get("code");
        URLConnection urlConnection = exchangeCodeForTokens(oktaIssuer, oktaClientId, redirectUri, code, pkceParameters);
        String response = readResponse(urlConnection);
        String tokenType = extractValueByPattern(TOKEN_TYPE_PATTERN, response);
        String accessToken = extractValueByPattern(ACCESS_TOKEN_PATTERN, response);
        return AuthTokenResponse.builder()
                .tokenType(tokenType)
                .accessToken(accessToken)
                .build();
    }

    @SneakyThrows
    private String authorizationUri(String oktaIssuer, String oktaClientId, String redirectUri, String sessionToken,
                                    Map<String, String> pkceParameters) {
        Map<String, String> queryParams = new LinkedHashMap<>();
        queryParams.put("client_id", oktaClientId);
        queryParams.put("response_type", "code");
        queryParams.put("response_mode", "fragment");
        queryParams.put("scope", "email+profile+openid");
        queryParams.put("redirect_uri", encode(redirectUri, UTF_8.name()));
        queryParams.put("state", "foo");
        queryParams.put("nonce", "bar");
        queryParams.put("sessionToken", sessionToken);
        queryParams.put(CODE_CHALLENGE_METHOD, pkceParameters.get(CODE_CHALLENGE_METHOD));
        queryParams.put(CODE_CHALLENGE, pkceParameters.get(CODE_CHALLENGE));
        String query = queryParams.entrySet().stream()
                .map(entry -> format("%s=%s", entry.getKey(), entry.getValue()))
                .collect(joining("&"));
        return oktaIssuer + "/v1/authorize?" + query;
    }

    @SneakyThrows
    private URLConnection exchangeCodeForTokens(String oktaIssuer, String oktaClientId, String redirectUri,
                                                String code, Map<String, String> pkceParameters) {
        String tokenUri = oktaIssuer + TOKEN_PATH;
        String content = createContent(oktaClientId, redirectUri, code, pkceParameters);
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE_HEADER, APPLICATION_X_WWW_FORM_URLENCODED);
        headers.put(ACCEPT_HEADER, APPLICATION_JSON);
        return HttpClientUtils.doPost(tokenUri, headers, content);
    }

    @SneakyThrows
    private String createContent(String oktaClientId, String redirectUri,
                                 String code, Map<String, String> pkceParameters) {
        String content = "grant_type=authorization_code";
        content += "&client_id=" + oktaClientId;
        content += "&redirect_uri=" + encode(redirectUri, UTF_8.name());
        content += "&code=" + code;
        content += "&code_verifier=" + pkceParameters.get(CODE_VERIFIER);
        return content;
    }
}
