package lv.gennadyyonov.hellookta.config.csrf;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import javax.servlet.http.HttpServletRequest;
import java.net.HttpCookie;

import static lv.gennadyyonov.hellookta.utils.RequestUtils.getHttpServletRequest;
import static org.springframework.http.HttpHeaders.COOKIE;

@RequiredArgsConstructor
public class CsrfTokenInterceptor implements RequestInterceptor {

    private final CsrfTokenRepository csrfTokenRepository;
    private final CsrfProperties csrfProperties;

    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest request = getHttpServletRequest();
        CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
        HttpCookie csrfCookie = new HttpCookie(csrfProperties.getCookieName(), csrfToken.getToken());
        template.header(COOKIE, csrfCookie.toString());
        template.header(csrfProperties.getHeaderName(), csrfToken.getToken());
    }
}
