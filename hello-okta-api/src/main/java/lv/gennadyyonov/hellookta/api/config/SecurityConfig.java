package lv.gennadyyonov.hellookta.api.config;

import lv.gennadyyonov.hellookta.services.TechnicalEndpointService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpMethod.OPTIONS;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ALL_URL_PATTERN = "/**";

    private final TechnicalEndpointService technicalEndpointService;

    public SecurityConfig(@Autowired(required = false) TechnicalEndpointService technicalEndpointService) {
        this.technicalEndpointService = technicalEndpointService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        authorizeTechnicalEndpoints(http);
        http
                .authorizeRequests()
                // Allow CORS option calls
                .antMatchers(OPTIONS, ALL_URL_PATTERN).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer().jwt();
    }

    private void authorizeTechnicalEndpoints(HttpSecurity http) throws Exception {
        String[] allowedEndpoints = ofNullable(technicalEndpointService)
                .map(TechnicalEndpointService::getAllowedEndpoints)
                .orElseGet(() -> new String[0]);
        if (ArrayUtils.isNotEmpty(allowedEndpoints)) {
            http
                    .authorizeRequests()
                    .antMatchers(allowedEndpoints)
                    .permitAll();
        }
    }
}
