package lv.gennadyyonov.hellookta.api.client;

import lombok.SneakyThrows;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
        String uri = config.getServerUri() + HELLO_PATH;
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getAccessToken());
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
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
