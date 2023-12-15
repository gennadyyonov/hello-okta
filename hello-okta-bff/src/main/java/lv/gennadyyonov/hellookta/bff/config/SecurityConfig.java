package lv.gennadyyonov.hellookta.bff.config;

import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.config.csrf.CsrfProperties;
import lv.gennadyyonov.hellookta.config.okta.LoggingAuthenticationEntryPoint;
import lv.gennadyyonov.hellookta.services.AuthenticationService;
import lv.gennadyyonov.hellookta.services.TechnicalEndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import static java.util.Optional.ofNullable;
import static lv.gennadyyonov.hellookta.bff.controller.EnvironmentConfigController.ENVIRONMENT_CONFIG_SUFFIX;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableConfigurationProperties(UserCacheProperties.class)
@Configuration
public class SecurityConfig {

    private static final String ALL_URL_PATTERN = "/**";

    private final JwtIssuerAuthenticationManagerResolver jwtIssuerAuthenticationManagerResolver;
    private final TechnicalEndpointService technicalEndpointService;
    private final CsrfProperties csrfProperties;
    private final Customizer<CsrfConfigurer<HttpSecurity>> csrfCustomizer;
    private final Customizer<HeadersConfigurer<HttpSecurity>> headersCustomizer;
    private final AuthenticationService authenticationService;
    private final UserCacheProperties userCacheProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
        configureCsrf(http);
        ofNullable(technicalEndpointService).ifPresent(service -> service.allowTechnicalEndpoints(http));
        var authenticationEntryPoint = new BearerTokenAuthenticationEntryPoint();
        http
            .headers(headersCustomizer)
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                // Allow CORS option calls
                .requestMatchers(antMatcher(OPTIONS, ALL_URL_PATTERN)).permitAll()
                .requestMatchers(antMatcher(GET, ENVIRONMENT_CONFIG_SUFFIX)).permitAll()
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth -> oauth
                .authenticationManagerResolver(jwtIssuerAuthenticationManagerResolver)
                .authenticationEntryPoint(new LoggingAuthenticationEntryPoint(authenticationEntryPoint)));
        return http.build();
    }

    private void configureCsrf(HttpSecurity http) throws Exception {
        if (toBoolean(csrfProperties.getCsrfEnabled())) {
            http.csrf(csrfCustomizer);
        } else {
            http.csrf(AbstractHttpConfigurer::disable);
        }
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        authenticationService.initUsers(auth, userCacheProperties.getUsers());
    }
}
