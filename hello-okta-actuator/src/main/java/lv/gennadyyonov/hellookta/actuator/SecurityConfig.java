package lv.gennadyyonov.hellookta.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@Order(50)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final HttpTraceFilter httpTraceFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterAfter(httpTraceFilter, SecurityContextPersistenceFilter.class)
                .requestMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeRequests(requests -> requests.anyRequest().permitAll());
    }
}
