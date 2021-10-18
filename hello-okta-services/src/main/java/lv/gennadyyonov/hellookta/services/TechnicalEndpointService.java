package lv.gennadyyonov.hellookta.services;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.config.TechnicalEndpointProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static lv.gennadyyonov.hellookta.utils.StreamUtils.getNullableFlatStream;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;

public class TechnicalEndpointService {

    private final Collection<Class<?>> allowedClasses;
    private final Collection<AntPathRequestMatcher> allowedRequestMatchers;
    private final boolean technicalEndpointEnabled;

    public TechnicalEndpointService(TechnicalEndpointProperties props) {
        technicalEndpointEnabled = toBoolean(props.getEnabled());
        allowedClasses = technicalEndpointEnabled ? getAllowedClasses(props) : emptySet();
        allowedRequestMatchers = technicalEndpointEnabled ? getAllowedRequestMatchers(props) : emptySet();
    }

    private Collection<Class<?>> getAllowedClasses(TechnicalEndpointProperties props) {
        return getEnabledEndpointFlatStream(props, TechnicalEndpointProperties.Endpoint::getAllowedClasses)
            .map(clazz -> {
                try {
                    return Class.forName(clazz);
                } catch (ClassNotFoundException ex) {
                    throw new IllegalArgumentException(ex);
                }
            })
            .collect(toSet());
    }

    private Collection<AntPathRequestMatcher> getAllowedRequestMatchers(TechnicalEndpointProperties props) {
        return getEnabledEndpointFlatStream(props, TechnicalEndpointProperties.Endpoint::getAllowedEndpoints)
            .map(AntPathRequestMatcher::new)
            .collect(toSet());
    }

    private <T> Stream<T> getEnabledEndpointFlatStream(TechnicalEndpointProperties techEndpointProps,
                                                       Function<TechnicalEndpointProperties.Endpoint, List<T>> func) {
        return getNullableFlatStream(techEndpointProps.getEndpoints())
            .filter(endpoint -> toBoolean(endpoint.getEnabled()))
            .map(func)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream);
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
    public void allowTechnicalEndpoints(HttpSecurity http) {
        if (!technicalEndpointEnabled) {
            return;
        }
        if (!allowedRequestMatchers.isEmpty()) {
            String[] allowedPatterns = allowedRequestMatchers.stream()
                .map(AntPathRequestMatcher::getPattern)
                .toArray(String[]::new);
            http
                .authorizeRequests()
                .antMatchers(allowedPatterns)
                .permitAll();
        }
    }

    public boolean isWhitelistedUrl(HttpServletRequest request) {
        if (!technicalEndpointEnabled) {
            return false;
        }
        return getNullableFlatStream(allowedRequestMatchers).anyMatch(matcher -> matcher.matches(request));
    }
}
