package lv.gennadyyonov.hellookta.bff.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.List;

import static lv.gennadyyonov.hellookta.bff.utils.AuthorizationUtils.authorizationHeader;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class AuthorizationHeaderInterceptor implements ClientHttpRequestInterceptor {

    private final String username;
    private final List<String> groups;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add(AUTHORIZATION, authorizationHeader(username, groups));
        return execution.execute(request, body);
    }
}
