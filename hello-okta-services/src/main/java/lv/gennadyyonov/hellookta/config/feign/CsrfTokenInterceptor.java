package lv.gennadyyonov.hellookta.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

import java.net.HttpCookie;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.COOKIE;

@RequiredArgsConstructor
public class CsrfTokenInterceptor implements RequestInterceptor {

    private final String cookieName;
    private final String headerName;

    @Override
    public void apply(RequestTemplate template) {
        String csrfValue = UUID.randomUUID().toString();
        HttpCookie csrfCookie = new HttpCookie(cookieName, csrfValue);
        template.header(COOKIE, csrfCookie.toString());
        template.header(headerName, csrfValue);
    }
}
