package lv.gennadyyonov.hellookta.config.okta;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver.jwt.jwk-set.cache")
@Validated
@RequiredArgsConstructor
@Value
public class OktaResourceServerJwkSetCacheProperties {

    @NotNull
    Long refreshTime;
    @NotNull
    Long refreshAheadTime;
    @NotNull
    Long refreshTimeout;
}
