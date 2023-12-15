package lv.gennadyyonov.hellookta.config.okta;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class OnJwkSetCacheCondition extends AllNestedConditions {
    public OnJwkSetCacheCondition() {
        super(ConfigurationPhase.PARSE_CONFIGURATION);
    }

    @ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
    static class HasJwkSetUri {
    }

    @ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.jwk-set.cache.enabled",
        havingValue = "true")
    static class HasJwkSetCacheEnabled {
    }
}
