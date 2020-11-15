package lv.gennadyyonov.hellookta.api.client;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.ACCEPT_HEADER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.APPLICATION_JSON;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.APPLICATION_X_WWW_FORM_URLENCODED;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.AUTHORIZATION_HEADER;
import static lv.gennadyyonov.hellookta.api.client.utils.HttpClientUtils.CONTENT_TYPE_HEADER;
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
        Map<String, String> headers = new HashMap<>();
        String authorization = createAuthorization(request.getClientId(), request.getClientSecret());
        headers.put(AUTHORIZATION_HEADER, "Basic " + authorization);
        headers.put(CONTENT_TYPE_HEADER, APPLICATION_X_WWW_FORM_URLENCODED);
        headers.put(ACCEPT_HEADER, APPLICATION_JSON);
        String tokenUri = request.getTokenUri();
        String content = createContent(request);
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
