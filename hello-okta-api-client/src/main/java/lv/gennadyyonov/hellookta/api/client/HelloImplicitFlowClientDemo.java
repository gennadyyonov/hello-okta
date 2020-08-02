package lv.gennadyyonov.hellookta.api.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lv.gennadyyonov.hellookta.api.client.common.EnvironmentConfigClient;
import lv.gennadyyonov.hellookta.api.client.common.EnvironmentProperties;
import lv.gennadyyonov.hellookta.api.client.common.AuthTokenRequest;
import lv.gennadyyonov.hellookta.api.client.common.AuthTokenResponse;
import lv.gennadyyonov.hellookta.api.client.implicit.AuthTokenResponseClient;
import lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.ACCEPT_HEADER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.APPLICATION_JSON;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.AUTHORIZATION_HEADER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.CONTENT_TYPE_HEADER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.doGet;
import static lv.gennadyyonov.hellookta.api.client.utils.LocalhostUtils.disableSSL;

@Slf4j
public class HelloImplicitFlowClientDemo {

    private static final String PROPERTIES_FILE = "/implicitFlow.properties";
    private static final String HELLO_PATH = "/hello";

    @SneakyThrows
    public static void main(String[] args) {
        disableSSL();
        Properties properties = loadProperties();
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        String orgUrl = properties.getProperty("orgUrl");
        String apiUri = properties.getProperty("apiUri");
        String bffUri = properties.getProperty("bffUri");
        String spaUri = properties.getProperty("spaUri");

        EnvironmentConfigClient environmentConfigClient = new EnvironmentConfigClient(bffUri);
        EnvironmentProperties environmentProperties = environmentConfigClient.getEnvironmentProperties();

        AuthTokenResponseClient authTokenResponseClient = new AuthTokenResponseClient();
        AuthTokenRequest authTokenRequest = AuthTokenRequest.builder()
                .spaUri(spaUri)
                .environmentProperties(environmentProperties)
                .orgUrl(orgUrl)
                .username(username)
                .password(password)
                .build();
        AuthTokenResponse tokenResponse = authTokenResponseClient.getTokenResponse(authTokenRequest);

        String uri = apiUri + HELLO_PATH;
        Map<String, String> headers = headers(tokenResponse);
        HttpURLConnection connection = doGet(uri, headers);
        String response = HttpClientUtils.readResponse(connection);
        log.info("Hello Response : {}", response);
    }

    @SneakyThrows
    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream in = HelloImplicitFlowClientDemo.class.getResourceAsStream(PROPERTIES_FILE)) {
            properties.load(in);
            return properties;
        }
    }

    private static Map<String, String> headers(AuthTokenResponse tokenResponse) {
        Map<String, String> headers = new HashMap<>();
        headers.put(AUTHORIZATION_HEADER, tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken());
        headers.put(CONTENT_TYPE_HEADER, APPLICATION_JSON);
        headers.put(ACCEPT_HEADER, APPLICATION_JSON);
        return headers;
    }
}
