package lv.gennadyyonov.hellookta.api.client;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.createAuthorization;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.readResponse;

public abstract class ClientCredentialsTokenResponseClient<T> {

    private final Function<String, T> responseFunction;

    public ClientCredentialsTokenResponseClient(Function<String, T> responseFunction) {
        this.responseFunction = responseFunction;
    }

    @SneakyThrows
    public T getTokenResponse(ClientCredentialsRequest request) {
        URLConnection connection = doPost(request);
        String response = readResponse(connection);
        return responseFunction.apply(response);
    }

    @SneakyThrows
    private URLConnection doPost(ClientCredentialsRequest request) {
        String tokenUri = request.getTokenUri();
        String content = createContent(request);
        Map<String, String> headers = new HashMap<>();
        String authorization = createAuthorization(request.getClientId(), request.getClientSecret());
        headers.put("Authorization", "Basic " + authorization);
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept", "application/json");
        return HttpClientUtils.doPost(tokenUri, headers, content);
    }

    private String createContent(ClientCredentialsRequest request) {
        String content = "grant_type=client_credentials";
        String scope = request.getScope();
        if (scope != null) {
            content += "&scope=" + scope;
        }
        return content;
    }
}
