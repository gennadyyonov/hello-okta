package lv.gennadyyonov.hellookta.services;

import com.nimbusds.jose.RemoteKeySourceException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSetCache;
import com.nimbusds.jose.util.Resource;
import com.nimbusds.jose.util.ResourceRetriever;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

@RequiredArgsConstructor
@Slf4j
public class JwkSetCacheService {

    private final ResourceRetriever jwkSetRetriever;
    private final JWKSetCache jwkSetCache;
    private final OAuth2ResourceServerProperties resourceServerProperties;

    @SneakyThrows
    public void refreshJwkSet() {
        OAuth2ResourceServerProperties.Jwt jwt = resourceServerProperties.getJwt();
        URL jwkSetUrl = new URL(jwt.getJwkSetUri());
        Resource res;
        try {
            res = jwkSetRetriever.retrieveResource(jwkSetUrl);
        } catch (IOException e) {
            throw new RemoteKeySourceException("Couldn't retrieve remote JWK set: " + e.getMessage(), e);
        }
        JWKSet jwkSet;
        try {
            jwkSet = JWKSet.parse(res.getContent());
        } catch (ParseException e) {
            throw new RemoteKeySourceException("Couldn't parse remote JWK set: " + e.getMessage(), e);
        }
        jwkSetCache.put(jwkSet);
    }
}
