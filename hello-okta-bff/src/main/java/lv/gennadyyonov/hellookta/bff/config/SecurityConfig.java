package lv.gennadyyonov.hellookta.bff.config;

import lv.gennadyyonov.hellookta.config.CustomAuthorizationRequestResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static lv.gennadyyonov.hellookta.bff.controller.EnvironmentConfigController.ENVIRONMENT_CONFIG_SUFFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.PRAGMA;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ALL = "*";
    private static final String ALL_URL_PATTERN = "/**";

    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                // Allow CORS option calls
                .antMatchers(OPTIONS, ALL_URL_PATTERN).permitAll()
                .antMatchers(GET, ENVIRONMENT_CONFIG_SUFFIX).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .authorizationRequestResolver(new CustomAuthorizationRequestResolver(
                        clientRegistrationRepository, DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
                ));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(singletonList(ALL));
        List<String> allowedMethods = Arrays.stream(HttpMethod.values())
                .map(HttpMethod::name)
                .collect(toList());
        configuration.setAllowedMethods(allowedMethods);
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*'
        // when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(asList(AUTHORIZATION, CONTENT_TYPE, CACHE_CONTROL, PRAGMA));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(ALL_URL_PATTERN, configuration);
        return source;
    }
}
