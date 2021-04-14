package lv.gennadyyonov.hellookta.aspects;

import lombok.extern.slf4j.Slf4j;
import lv.gennadyyonov.hellookta.constants.SecurityConstants;
import lv.gennadyyonov.hellookta.exception.AccessDeniedException;
import lv.gennadyyonov.hellookta.services.AuthenticationService;
import lv.gennadyyonov.hellookta.services.SecurityService;
import lv.gennadyyonov.hellookta.services.TechnicalEndpointService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.springframework.core.annotation.AnnotatedElementUtils.getMergedAnnotation;

@Aspect
@Slf4j
public class SecurityRoleAspect {

    private static final String USER_HAS_NOT_ACCESS_MESSAGE_FORMAT = "User '%s' has no access to %s";

    private final AuthenticationService authenticationService;
    private final SecurityService securityService;
    private final TechnicalEndpointService technicalEndpointService;

    public SecurityRoleAspect(AuthenticationService authenticationService,
                              SecurityService securityService,
                              @Autowired(required = false) TechnicalEndpointService technicalEndpointService) {
        this.authenticationService = authenticationService;
        this.securityService = securityService;
        this.technicalEndpointService = technicalEndpointService;
    }

    @Pointcut("(@within(org.springframework.stereotype.Controller) || "
            + "@within(org.springframework.web.bind.annotation.RestController)) && "
            + "!within(org.springframework.boot.autoconfigure.web.servlet.error.*)")
    public void controllers() {
        // Do nothing. Method for declaration
    }

    @Pointcut("(@annotation(HasRole) || @within(HasRole))")
    public void annotated() {
        // Do nothing. Method for declaration
    }

    @Around("controllers() && !annotated()")
    public Object restrictByAllowedUserRoles(ProceedingJoinPoint joinPoint) throws Throwable {
        if (isSecured(joinPoint) && !securityService.hasAnyRoles(SecurityConstants.ALLOWED_USERS, new String[0])) {
            String userId = authenticationService.getUserId();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String message = format(USER_HAS_NOT_ACCESS_MESSAGE_FORMAT, userId, getMethodFullName(methodSignature));
            log.error(message);
            throw new AccessDeniedException(message);
        }

        return joinPoint.proceed();
    }

    @Around("annotated()")
    public Object restrictByRole(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        HasRole annotation = getHasRoleAnnotation(methodSignature);
        if (annotation != null &&
                (isSecured(joinPoint) && !securityService.hasAnyRoles(annotation.alias(), annotation.roles()))) {
            String userId = authenticationService.getUserId();
            String message = format(USER_HAS_NOT_ACCESS_MESSAGE_FORMAT, userId, getMethodFullName(methodSignature));
            log.error(message);
            throw new AccessDeniedException(message);
        }

        return joinPoint.proceed();
    }

    private String getMethodFullName(MethodSignature methodSignature) {
        return methodSignature.toShortString();
    }

    private HasRole getHasRoleAnnotation(MethodSignature methodSignature) {
        HasRole annotation = getMergedAnnotation(methodSignature.getMethod(), HasRole.class);
        return ofNullable(annotation).orElse(getMergedAnnotation(methodSignature.getMethod().getDeclaringClass(), HasRole.class));
    }

    private boolean isSecured(ProceedingJoinPoint joinPoint) {
        return ofNullable(technicalEndpointService)
                .map(service -> !service.isAllowed(joinPoint))
                .orElse(true);
    }
}
