package lv.gennadyyonov.hellookta.services;

import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN;

public interface TechnicalEndpointService {

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

    String[] getAllowedEndpoints();
}
