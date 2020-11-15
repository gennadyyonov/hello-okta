package lv.gennadyyonov.hellookta.api.client;

import lombok.SneakyThrows;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.ACCEPT_HEADER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.APPLICATION_JSON;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.AUTHORIZATION_HEADER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.CONTENT_TYPE_HEADER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.doGet;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.readResponse;

public abstract class HelloClient<T> {

    private static final String TOKEN_PATH = "/v1/token";
    private static final String HELLO_PATH = "/hello";

    private final ClientConfig config;
    private final Function<String, T> responseFunction;

    public HelloClient(ClientConfig config, Function<String, T> responseFunction) {
        this.config = config;
        this.responseFunction = responseFunction;
    }

    public T hello() {
        URLConnection connection = doHello();
        String response = readResponse(connection);
        return responseFunction.apply(response);
    }

    private URLConnection doHello() {
        Map<String, String> headers = new HashMap<>();
        headers.put(AUTHORIZATION_HEADER, "Bearer " + getAccessToken());
        headers.put(CONTENT_TYPE_HEADER, APPLICATION_JSON);
        headers.put(ACCEPT_HEADER, APPLICATION_JSON);
        String uri = config.getServerUri() + HELLO_PATH;
        return doGet(uri, headers);
    }

    @SneakyThrows
    private String getAccessToken() {
        String tokenUri = config.getIssuer() + TOKEN_PATH;
        ClientCredentialsRequest request = ClientCredentialsRequest.builder()
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .scope(config.getScope())
                .tokenUri(tokenUri)
                .build();
        return getAccessToken(request);
    }

    protected abstract String getAccessToken(ClientCredentialsRequest request);
}
