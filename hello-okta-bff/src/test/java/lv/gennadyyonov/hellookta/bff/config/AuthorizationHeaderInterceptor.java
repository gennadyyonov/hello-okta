package lv.gennadyyonov.hellookta.bff.config;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static lv.gennadyyonov.hellookta.bff.utils.AuthorizationUtils.authorizationHeader;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class AuthorizationHeaderInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add(AUTHORIZATION, authorizationHeader());
        return execution.execute(request, body);
    }
}
