package lv.gennadyyonov.hellookta.services;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.config.TechnicalEndpointProperties;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static lv.gennadyyonov.hellookta.utils.StreamUtils.getNullableFlatStream;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN;

public class TechnicalEndpointService {

    private final Collection<String> referrerHeaderNames;
    private final Collection<Class<?>> allowedClasses;
    private final String[] allowedEndpoints;
    private final boolean technicalEndpointEnabled;

    public TechnicalEndpointService(TechnicalEndpointProperties techEndpointProps) {
        technicalEndpointEnabled = toBoolean(techEndpointProps.getEnabled());
        referrerHeaderNames = technicalEndpointEnabled ? getReferrerHeaderNames(techEndpointProps) : Collections.emptyList();
        allowedClasses = technicalEndpointEnabled ? getAllowedClasses(techEndpointProps) : Collections.emptySet();
        allowedEndpoints = technicalEndpointEnabled ? getAllowedEndpoints(techEndpointProps) : new String[0];
    }

    private Collection<String> getReferrerHeaderNames(TechnicalEndpointProperties techEndpointProps) {
        return getNullableFlatStream(techEndpointProps.getReferrerHeaderNames()).collect(toList());
    }

    private Collection<Class<?>> getAllowedClasses(TechnicalEndpointProperties techEndpointProps) {
        return getNullableFlatStream(techEndpointProps.getEndpoints())
            .filter(endpoint -> toBoolean(endpoint.getEnabled()))
            .map(TechnicalEndpointProperties.Endpoint::getAllowedClasses)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .map(clazz -> {
                try {
                    return Class.forName(clazz);
                } catch (ClassNotFoundException ex) {
                    throw new IllegalArgumentException(ex);
                }
            })
            .collect(toSet());
    }

    private String[] getAllowedEndpoints(TechnicalEndpointProperties techEndpointProps) {
        return getNullableFlatStream(techEndpointProps.getEndpoints())
            .filter(endpoint -> toBoolean(endpoint.getEnabled()))
            .map(TechnicalEndpointProperties.Endpoint::getAllowedEndpoints)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .collect(toSet())
            .toArray(String[]::new);
    }

    public boolean isAllowed(ProceedingJoinPoint joinPoint) {
        if (!technicalEndpointEnabled) {
            return false;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> clazz = signature.getMethod().getDeclaringClass();
        return allowedClasses.stream().anyMatch(allowedClass -> allowedClass.isAssignableFrom(clazz));
    }

    @SneakyThrows
    public void configure(HttpSecurity http) {
        if (!technicalEndpointEnabled) {
            return;
        }
        if (ArrayUtils.isNotEmpty(allowedEndpoints)) {
            http
                .authorizeRequests()
                .antMatchers(allowedEndpoints)
                .permitAll();
            http.headers().referrerPolicy(SAME_ORIGIN);
        }
    }

    public boolean isWhitelistedUrl(HttpServletRequest request, List<String> endpoints) {
        if (!technicalEndpointEnabled) {
            return false;
        }
        List<String> referrers = referrerHeaderNames.stream()
            .map(request::getHeader)
            .filter(Objects::nonNull)
            .collect(toList());
        String requestUri = request.getRequestURI();
        return getNullableFlatStream(endpoints)
            .anyMatch(endpoint -> requestUri.contains(endpoint) || referrers.stream()
                .anyMatch(referrer -> referrer.contains(endpoint)));
    }
}
