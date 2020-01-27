package lv.gennadyyonov.hellookta.services;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lv.gennadyyonov.hellookta.connectors.UserInfoConnector;
import lv.gennadyyonov.hellookta.dto.UserInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.StandardClaimAccessor;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static lv.gennadyyonov.hellookta.utils.OktaUtils.extractCollection;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.ADDRESS;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.EMAIL;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.PHONE;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.PROFILE;
import static org.springframework.util.CollectionUtils.containsAny;

@Slf4j
public class UserInfoService {

    private static final String USER_INFO_PATH = "/v1/userinfo";
    private static final String SCOPES_CLAIM = "scp";
    private static final Collection<String> USER_INFO_SCOPES = asList(PROFILE, EMAIL, ADDRESS, PHONE);

    private final AuthenticationService authenticationService;
    private final String userInfoUri;
    private final UserInfoConnector userInfoConnector;

    public UserInfoService(AuthenticationService authenticationService,
                           String issuerUri,
                           UserInfoConnector userInfoConnector) {
        this.authenticationService = authenticationService;
        this.userInfoUri = issuerUri + USER_INFO_PATH;
        this.userInfoConnector = userInfoConnector;
    }

    public UserInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OktaProfile oktaProfile = getOktaProfile(authentication);
        return UserInfo.builder()
                .userId(authenticationService.getUserId(authentication))
                .firstName(oktaProfile.getGivenName())
                .lastName(oktaProfile.getFamilyName())
                .email(oktaProfile.getEmail())
                .roles(oktaProfile.getAuthorities())
                .build();
    }

    private OktaProfile getOktaProfile(Authentication authentication) {
        Map<String, Object> tokenAttributes = authenticationService.getTokenAttributes(authentication);
        log.debug("User attributes from authentication token: {}", tokenAttributes);
        Map<String, Object> userInfoAttributes = loadUserInfoAttributes(authentication, tokenAttributes);
        log.debug("User Info attributes: {}", userInfoAttributes);
        Set<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toSet());
        log.info("Authorities: {}", authorities);
        Map<String, Object> attributes = new HashMap<>();
        attributes.putAll(tokenAttributes);
        attributes.putAll(userInfoAttributes);
        return new OktaProfile(attributes, authorities);
    }

    @SneakyThrows
    private Map<String, Object> loadUserInfoAttributes(Authentication authentication, Map<String, Object> tokenAttributes) {
        Collection<String> scopes = extractCollection(tokenAttributes, SCOPES_CLAIM);
        if (containsAny(scopes, USER_INFO_SCOPES)) {
            URI baseUri = new URI(userInfoUri);
            Map<String, Object> headers = new HashMap<>();
            headers.put(HttpHeaders.AUTHORIZATION, authenticationService.authorizationHeaderValue(authentication));
            return userInfoConnector.getUserInfo(baseUri, headers);
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
