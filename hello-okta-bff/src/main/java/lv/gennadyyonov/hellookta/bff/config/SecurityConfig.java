package lv.gennadyyonov.hellookta.bff.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static lv.gennadyyonov.hellookta.bff.config.OktaConfig.HELLO_OKTA_BFF_PROPS_BEAN_NAME;
import static lv.gennadyyonov.hellookta.bff.controller.EnvironmentConfigController.ENVIRONMENT_CONFIG_SUFFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.PRAGMA;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ALL_URL_PATTERN = "/**";
    private static final String SESSION_ID_COOKIE_NAME = "JSESSIONID";
    public static final String ALLOWED_ORIGINS_SEPARATOR = ",";

    private final HelloOktaBffProps helloOktaBffProps;

    public SecurityConfig(@Qualifier(HELLO_OKTA_BFF_PROPS_BEAN_NAME) HelloOktaBffProps helloOktaBffProps) {
        this.helloOktaBffProps = helloOktaBffProps;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        configureCsrf(http);
        http
                .cors()
                .and()
                .authorizeRequests()
                // Allow CORS option calls
                .antMatchers(OPTIONS, ALL_URL_PATTERN).permitAll()
                .antMatchers(GET, ENVIRONMENT_CONFIG_SUFFIX).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies(SESSION_ID_COOKIE_NAME)
                .and()
                .oauth2Login()
                .and()
                .oauth2ResourceServer().jwt();
    }

    private void configureCsrf(HttpSecurity http) throws Exception {
        if (TRUE.equals(helloOktaBffProps.getCsrfEnabled())) {
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        } else {
            http.csrf().disable();
        }
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        List<String> allowedOrigins = ofNullable(helloOktaBffProps.getAllowedOrigins())
                .map(origins -> origins.split(ALLOWED_ORIGINS_SEPARATOR))
                .map(Stream::of)
                .orElseGet(Stream::empty)
                .collect(toList());
        configuration.setAllowedOrigins(allowedOrigins);
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
