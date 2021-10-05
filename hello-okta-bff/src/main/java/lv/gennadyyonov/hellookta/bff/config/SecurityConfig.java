package lv.gennadyyonov.hellookta.bff.config;

import lv.gennadyyonov.hellookta.services.TechnicalEndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
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
    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";
    public static final String ALLOWED_ORIGINS_SEPARATOR = ",";

    private final HelloOktaBffProps helloOktaBffProps;
    private final TechnicalEndpointService technicalEndpointService;

    public SecurityConfig(HelloOktaBffProps helloOktaBffProps,
                          @Autowired(required = false) TechnicalEndpointService technicalEndpointService) {
        this.helloOktaBffProps = helloOktaBffProps;
        this.technicalEndpointService = technicalEndpointService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        configureCsrf(http);
        ofNullable(technicalEndpointService).ifPresent(service -> service.configure(http));
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
            .oauth2ResourceServer().jwt();
        // For auth throug BFF index.html
        http.oauth2Login()
            .and()
            .logout()
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies(SESSION_ID_COOKIE_NAME, CSRF_COOKIE_NAME);
    }

    private void configureCsrf(HttpSecurity http) throws Exception {
        if (TRUE.equals(helloOktaBffProps.getCsrfEnabled())) {
            http.csrf(csrf -> csrf
                .csrfTokenRepository(getCsrfTokenRepository())
                // https://github.com/spring-projects/spring-security/issues/8668
                .withObjectPostProcessor(new ObjectPostProcessor<CsrfFilter>() {
                    @Override
                    public <T extends CsrfFilter> T postProcess(T object) {
                        object.setRequireCsrfProtectionMatcher(CsrfFilter.DEFAULT_CSRF_MATCHER);
                        return object;
                    }
                })
            );
        } else {
            http.csrf().disable();
        }
    }

    private CookieCsrfTokenRepository getCsrfTokenRepository() {
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookieName(CSRF_COOKIE_NAME);
        csrfTokenRepository.setHeaderName(CSRF_HEADER_NAME);
        csrfTokenRepository.setCookiePath("/");
        return csrfTokenRepository;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        List<String> allowedOrigins = ofNullable(helloOktaBffProps.getAllowedOrigins())
            .map(origins -> origins.split(ALLOWED_ORIGINS_SEPARATOR))
            .stream()
            .flatMap(Stream::of)
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
        configuration.setAllowedHeaders(asList(AUTHORIZATION, CONTENT_TYPE, CACHE_CONTROL, PRAGMA, CSRF_HEADER_NAME));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(ALL_URL_PATTERN, configuration);
        return source;
    }
}
