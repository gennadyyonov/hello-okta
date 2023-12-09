package lv.gennadyyonov.hellookta.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lv.gennadyyonov.hellookta.connectors.UserInfoConnector;
import lv.gennadyyonov.hellookta.dto.UserInfo;
import org.springframework.security.oauth2.core.oidc.StandardClaimAccessor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static lv.gennadyyonov.hellookta.utils.OktaUtils.extractCollection;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.ADDRESS;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.EMAIL;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.PHONE;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.PROFILE;
import static org.springframework.util.CollectionUtils.containsAny;

@Slf4j
@RequiredArgsConstructor
public class UserInfoService {

    private static final String SCOPES_CLAIM = "scp";
    private static final Collection<String> USER_INFO_SCOPES = asList(PROFILE, EMAIL, ADDRESS, PHONE);

    private final AuthenticationService authenticationService;
    private final UserInfoConnector userInfoConnector;

    public UserInfo getUserInfo() {
        OktaProfile oktaProfile = getOktaProfile();
        return UserInfo.builder()
                .userId(authenticationService.getUserId())
                .firstName(oktaProfile.getGivenName())
                .lastName(oktaProfile.getFamilyName())
                .email(oktaProfile.getEmail())
                .roles(oktaProfile.getAuthorities())
                .build();
    }

    private OktaProfile getOktaProfile() {
        Map<String, Object> tokenAttributes = authenticationService.getTokenAttributes();
        log.debug("User attributes from authentication token: {}", tokenAttributes);
        Map<String, Object> userInfoAttributes = loadUserInfoAttributes(tokenAttributes);
        log.debug("User Info attributes: {}", userInfoAttributes);
        Set<String> authorities = authenticationService.getAuthorities();
        log.info("Authorities: {}", authorities);
        Map<String, Object> attributes = new HashMap<>();
        attributes.putAll(tokenAttributes);
        attributes.putAll(userInfoAttributes);
        return new OktaProfile(attributes, authorities);
    }

    @SneakyThrows
    private Map<String, Object> loadUserInfoAttributes(Map<String, Object> tokenAttributes) {
        Collection<String> scopes = extractCollection(tokenAttributes, SCOPES_CLAIM);
        if (containsAny(scopes, USER_INFO_SCOPES)) {
            Map<String, Object> headers = new HashMap<>();
            headers.put(AUTHORIZATION, authenticationService.authorizationHeaderValue());
            return userInfoConnector.getUserInfo(headers);
        }
        return new HashMap<>();
    }

    @Data
    private static class OktaProfile implements StandardClaimAccessor {

        private final Map<String, Object> claims;
        private final Set<String> authorities;

        private OktaProfile(Map<String, Object> claims, Set<String> authorities) {
            this.claims = claims;
            this.authorities = authorities;
        }
    }
}
