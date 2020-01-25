package lv.gennadyyonov.hellookta.services;

import lombok.Data;
import org.springframework.security.oauth2.core.oidc.StandardClaimAccessor;

import java.util.Map;
import java.util.Set;

@Data
public class OktaProfile implements StandardClaimAccessor {

    private final Map<String, Object> claims;
    private final Set<String> authorities;

    public OktaProfile(Map<String, Object> claims, Set<String> authorities) {
        this.claims = claims;
        this.authorities = authorities;
    }
}
