package lv.gennadyyonov.hellookta.services;

import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static lv.gennadyyonov.hellookta.utils.StreamUtils.getNullableFlatStream;
import static org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN;

public interface TechnicalEndpointService {

    List<String> REFERRER_HEADER_NAMES = singletonList(HttpHeaders.REFERER);

    boolean isAllowed(ProceedingJoinPoint joinPoint);

    @SneakyThrows
    default void configure(HttpSecurity http) {
        String[] allowedEndpoints = getAllowedEndpoints();
        if (ArrayUtils.isNotEmpty(allowedEndpoints)) {
            http
                .authorizeRequests()
                .antMatchers(allowedEndpoints)
                .permitAll();
            http.headers().referrerPolicy(SAME_ORIGIN);
        }
    }

    default boolean isWhitelistedUrl(HttpServletRequest request, List<String> endpoints) {
        if (!environmentSupported()) {
            return false;
        }
        List<String> referrers = REFERRER_HEADER_NAMES.stream()
            .map(request::getHeader)
            .filter(Objects::nonNull)
            .collect(toList());
        String requestUri = request.getRequestURI();
        return getNullableFlatStream(endpoints)
            .anyMatch(endpoint -> requestUri.contains(endpoint) || referrers.stream()
                .anyMatch(referrer -> referrer.contains(endpoint)));
    }

    boolean environmentSupported();

    String[] getAllowedEndpoints();
}
