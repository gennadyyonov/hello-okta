package lv.gennadyyonov.hellookta.config.okta;

import com.nimbusds.jose.jwk.source.CachingJWKSetSource;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.JWSAlgorithmFamilyJWSKeySelector;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jose.util.events.Event;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTProcessor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static lv.gennadyyonov.hellookta.utils.OktaUtils.getIssuerUri;

@ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
@Configuration
@Slf4j
public class OktaResourceServerConfig {

    private static final int MILLIS_PER_SECOND = 1000;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int JWK_SET_CACHE_REFRESH_TIME_MILLIS = 5 * SECONDS_PER_MINUTE * MILLIS_PER_SECOND;
    private static final int CACHE_REFRESH_AHEAD_TIME_MILLIS = 5 * SECONDS_PER_MINUTE * MILLIS_PER_SECOND;
    private static final int CACHE_REFRESH_TIMEOUT = 1500;

    @Bean
    public JwtIssuerAuthenticationManagerResolver jwtIssuerAuthenticationManagerResolver(OAuth2ClientProperties oktaOAuth2Properties) {
        var issuer = getIssuerUri(oktaOAuth2Properties);
        var authenticationManagers = Stream.of(issuer)
            .collect(Collectors.toMap(Function.identity(), this::getAuthenticationManager));
        return new JwtIssuerAuthenticationManagerResolver(new TrustedIssuerJwtAuthenticationManagerResolver(authenticationManagers));
    }

    @SneakyThrows
    private AuthenticationManager getAuthenticationManager(String issuer) {
        var jwkSetUrl = new URI(issuer + "/v1/keys").toURL();
        var jwtDecoder = jwtDecoder(jwkSetUrl);
        return new JwtAuthenticationProvider(jwtDecoder)::authenticate;
    }

    private JwtDecoder jwtDecoder(URL jwkSetUrl) {
        var jwkSetRetriever = jwkSetRetriever();
        var jwtSource = JWKSourceBuilder.create(jwkSetUrl, jwkSetRetriever)
            .cache(JWK_SET_CACHE_REFRESH_TIME_MILLIS + CACHE_REFRESH_AHEAD_TIME_MILLIS, CACHE_REFRESH_TIMEOUT)
            .refreshAheadCache(CACHE_REFRESH_AHEAD_TIME_MILLIS, true, this::refreshCacheEventListener)
            .rateLimited(0)
            .build();
        var jwtProcessor = processor(jwtSource);
        return new NimbusJwtDecoder(jwtProcessor);
    }

    private void refreshCacheEventListener(Event<CachingJWKSetSource<SecurityContext>, SecurityContext> event) {
        if (event instanceof CachingJWKSetSource.RefreshInitiatedEvent<SecurityContext>) {
            log.info("Refreshing JWK Set Cache...");
        }
    }

    private ResourceRetriever jwkSetRetriever() {
        RestOperations restOperations = new RestTemplate();
        return new RestOperationsResourceRetriever(restOperations);
    }

    @SneakyThrows
    private JWTProcessor<SecurityContext> processor(JWKSource<SecurityContext> jwkSource) {
        JWSKeySelector<SecurityContext> jwsKeySelector = JWSAlgorithmFamilyJWSKeySelector.fromJWKSource(jwkSource);
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        return jwtProcessor;
    }
}
