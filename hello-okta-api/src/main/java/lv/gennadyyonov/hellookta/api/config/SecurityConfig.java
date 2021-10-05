package lv.gennadyyonov.hellookta.api.config;

import lv.gennadyyonov.hellookta.services.TechnicalEndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpMethod.OPTIONS;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ALL_URL_PATTERN = "/**";
    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";

    private final TechnicalEndpointService technicalEndpointService;

    public SecurityConfig(@Autowired(required = false) TechnicalEndpointService technicalEndpointService) {
        this.technicalEndpointService = technicalEndpointService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        configureCsrf(http);
        ofNullable(technicalEndpointService).ifPresent(service -> service.configure(http));
        http
            .authorizeRequests()
            // Allow CORS option calls
            .antMatchers(OPTIONS, ALL_URL_PATTERN).permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .oauth2ResourceServer().jwt();
    }

    private void configureCsrf(HttpSecurity http) throws Exception {
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
    }

    private CookieCsrfTokenRepository getCsrfTokenRepository() {
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookieName(CSRF_COOKIE_NAME);
        csrfTokenRepository.setHeaderName(CSRF_HEADER_NAME);
        csrfTokenRepository.setCookiePath("/");
        return csrfTokenRepository;
    }
}
