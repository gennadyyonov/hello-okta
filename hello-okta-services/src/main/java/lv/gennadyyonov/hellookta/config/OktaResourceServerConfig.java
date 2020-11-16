package lv.gennadyyonov.hellookta.config;

import com.nimbusds.jose.jwk.source.DefaultJWKSetCache;
import com.nimbusds.jose.jwk.source.JWKSetCache;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSAlgorithmFamilyJWSKeySelector;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.Resource;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTProcessor;
import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.services.JwkSetCacheService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.util.Assert;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
@Configuration
public class OktaResourceServerConfig {

    private static final String JWK_SET_RETRIEVER = "jwkSetRetriever";
    private static final String JWK_SET_CACHE = "jwkSetCache";

    private static final int NO_EXPIRATION = -1;
    private static final int NO_REFRESH = -1;

    /**
     * {@link OAuth2ResourceServerConfigurer} and {@link NimbusJwtDecoder.JwkSetUriJwtDecoderBuilder}
     * currently do not allow customizing {@link JWKSetCache}.
     * First extension point to have our own {@link JWKSetCache} is have {@link JwtDecoder} re-configured.
     */
    @Bean
    public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties resourceServerProperties,
                                 @Qualifier(JWK_SET_RETRIEVER) ResourceRetriever jwkSetRetriever,
                                 @Qualifier(JWK_SET_CACHE) JWKSetCache jwkSetCache) {
        OAuth2ResourceServerProperties.Jwt properties = resourceServerProperties.getJwt();
        JWTProcessor<SecurityContext> jwtProcessor = processor(properties, jwkSetRetriever, jwkSetCache);
        return new NimbusJwtDecoder(jwtProcessor);
    }

    @SneakyThrows
    private JWTProcessor<SecurityContext> processor(OAuth2ResourceServerProperties.Jwt properties,
                                                    ResourceRetriever jwkSetRetriever,
                                                    JWKSetCache jwkSetCache) {
        URL jwkSetUrl = new URL(properties.getJwkSetUri());
        JWKSource<SecurityContext> jwkSource = new RemoteJWKSet<>(jwkSetUrl, jwkSetRetriever, jwkSetCache);
        JWSKeySelector<SecurityContext> jwsKeySelector = JWSAlgorithmFamilyJWSKeySelector.fromJWKSource(jwkSource);
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        return jwtProcessor;
    }

    @Bean(JWK_SET_RETRIEVER)
    public ResourceRetriever jwkSetRetriever() {
        RestOperations restOperations = new RestTemplate();
        return new RestOperationsResourceRetriever(restOperations);
    }

    /**
     * Cache with no expiration.
     */
    @Bean(JWK_SET_CACHE)
    public JWKSetCache jwkSetCache() {
        return new DefaultJWKSetCache(NO_EXPIRATION, NO_REFRESH, null);
    }

    @Bean
    public JwkSetCacheService jwkSetCacheService(@Qualifier(JWK_SET_RETRIEVER) ResourceRetriever jwkSetRetriever,
                                                 @Qualifier(JWK_SET_CACHE) JWKSetCache jwkSetCache,
                                                 OAuth2ResourceServerProperties resourceServerProperties) {
        return new JwkSetCacheService(jwkSetRetriever, jwkSetCache, resourceServerProperties);
    }

    /**
     * Copied from the {@link NimbusJwtDecoder} private static class with the same name.
     */
    private static class RestOperationsResourceRetriever implements ResourceRetriever {
        private static final MediaType APPLICATION_JWK_SET_JSON = new MediaType("application", "jwk-set+json");
        private final RestOperations restOperations;

        RestOperationsResourceRetriever(RestOperations restOperations) {
            Assert.notNull(restOperations, "restOperations cannot be null");
            this.restOperations = restOperations;
        }

        @Override
        public Resource retrieveResource(URL url) throws IOException {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, APPLICATION_JWK_SET_JSON));

            ResponseEntity<String> response;
            try {
                RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, url.toURI());
                response = this.restOperations.exchange(request, String.class);
            } catch (Exception ex) {
                throw new IOException(ex);
            }

            if (response.getStatusCodeValue() != HttpStatus.OK.value()) {
                throw new IOException(response.toString());
            }

            //noinspection ConstantConditions
            return new Resource(response.getBody(), StandardCharsets.UTF_8.name());
        }
    }
}
