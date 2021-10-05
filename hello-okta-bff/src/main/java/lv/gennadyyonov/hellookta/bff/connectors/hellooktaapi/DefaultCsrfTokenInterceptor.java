package lv.gennadyyonov.hellookta.bff.connectors.hellooktaapi;

import lv.gennadyyonov.hellookta.config.feign.CsrfTokenInterceptor;

import static lv.gennadyyonov.hellookta.bff.config.SecurityConfig.CSRF_COOKIE_NAME;
import static lv.gennadyyonov.hellookta.bff.config.SecurityConfig.CSRF_HEADER_NAME;

public class DefaultCsrfTokenInterceptor extends CsrfTokenInterceptor {

    public DefaultCsrfTokenInterceptor() {
        super(CSRF_COOKIE_NAME, CSRF_HEADER_NAME);
    }
}
