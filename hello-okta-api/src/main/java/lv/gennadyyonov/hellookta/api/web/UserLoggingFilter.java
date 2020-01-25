package lv.gennadyyonov.hellookta.api.web;

import lv.gennadyyonov.hellookta.api.service.SecurityService;
import org.slf4j.MDC;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

public class UserLoggingFilter extends AbstractRequestLoggingFilter {

    private static final String MDC_USER_KEY = "userAuth";

    private final SecurityService securityService;

    public UserLoggingFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        String userId = securityService.getUserId();
        MDC.put(MDC_USER_KEY, userId);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        MDC.remove(MDC_USER_KEY);
    }
}
