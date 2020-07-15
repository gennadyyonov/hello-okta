package lv.gennadyyonov.hellookta.api.config;

import lv.gennadyyonov.hellookta.config.CustomAuthorizationRequestResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ALL_URL_PATTERN = "/**";

    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // Allow CORS option calls
                .antMatchers(OPTIONS, ALL_URL_PATTERN).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .authorizationRequestResolver(new CustomAuthorizationRequestResolver(
                        clientRegistrationRepository, DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
                ));
    }
}
