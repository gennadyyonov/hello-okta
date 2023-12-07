package lv.gennadyyonov.hellookta.api.config;

import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.config.csrf.CsrfProperties;
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
import org.springframework.security.web.SecurityFilterChain;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableConfigurationProperties(UserCacheProperties.class)
@Configuration
public class SecurityConfig {

    private static final String ALL_URL_PATTERN = "/**";

    private final TechnicalEndpointService technicalEndpointService;
    private final CsrfProperties csrfProperties;
    private final Customizer<CsrfConfigurer<HttpSecurity>> csrfCustomizer;
    private final AuthenticationService authenticationService;
    private final UserCacheProperties userCacheProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
        configureCsrf(http);
        ofNullable(technicalEndpointService).ifPresent(service -> service.allowTechnicalEndpoints(http));
        http
            .authorizeHttpRequests(auth -> auth
                // Allow CORS option calls
                .requestMatchers(OPTIONS, ALL_URL_PATTERN).permitAll()
                .anyRequest().authenticated())
            // Allow CORS option calls
            .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()));
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
