package lv.gennadyyonov.hellookta.web;

import jakarta.servlet.http.HttpServletRequest;
import lv.gennadyyonov.hellookta.services.AuthenticationService;
import org.slf4j.MDC;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

public class UserLoggingFilter extends AbstractRequestLoggingFilter {

    private static final String MDC_USER_KEY = "userAuth";

    private final AuthenticationService authenticationService;

    public UserLoggingFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        String userId = authenticationService.getUserId();
        MDC.put(MDC_USER_KEY, userId);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        MDC.remove(MDC_USER_KEY);
    }
}
