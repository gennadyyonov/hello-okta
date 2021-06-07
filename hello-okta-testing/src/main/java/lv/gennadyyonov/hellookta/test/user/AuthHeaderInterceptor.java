package lv.gennadyyonov.hellookta.test.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

@RequiredArgsConstructor
public class AuthHeaderInterceptor implements ClientHttpRequestInterceptor {

    private final String username;
    private final List<String> groups;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add(AUTHORIZATION, BEARER.getValue() + " " + JwtToken.createCompact(username, groups));
        return execution.execute(request, body);
    }
}
