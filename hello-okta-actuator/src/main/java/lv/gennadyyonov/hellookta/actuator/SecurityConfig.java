package lv.gennadyyonov.hellookta.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

@RequiredArgsConstructor
@EnableConfigurationProperties(ProxyProperties.class)
@Configuration(proxyBeanMethods = false)
@Order(50)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final HttpTraceFilter httpTraceFilter;
    private final ProxyProperties proxyProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .addFilterAfter(httpTraceFilter, SecurityContextPersistenceFilter.class)
            .requestMatcher(new OrRequestMatcher(getRequestMatchers()))
            .authorizeRequests(requests -> requests.anyRequest().permitAll());
    }

    private List<RequestMatcher> getRequestMatchers() {
        List<RequestMatcher> requestMatchers = new ArrayList<>();
        requestMatchers.add(EndpointRequest.toAnyEndpoint());
        if (TRUE.equals(proxyProperties.getEnabled())) {
            requestMatchers.add(new AntPathRequestMatcher(proxyProperties.getPath() + "/**"));
        }
        return requestMatchers;
    }
}
