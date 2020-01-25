package lv.gennadyyonov.hellookta.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static lv.gennadyyonov.hellookta.services.OktaUtils.extractCollection;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.ADDRESS;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.EMAIL;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.PHONE;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.PROFILE;
import static org.springframework.util.CollectionUtils.containsAny;

@Slf4j
public class OktaService {

    private static final String SCOPES_CLAIM = "scp";
    private static final Collection<String> USER_INFO_SCOPES = asList(PROFILE, EMAIL, ADDRESS, PHONE);

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final OktaConnector oktaConnector;

    public OktaService(OAuth2AuthorizedClientService authorizedClientService, OktaConnector oktaConnector) {
        this.authorizedClientService = authorizedClientService;
        this.oktaConnector = oktaConnector;
    }

    public String getTokenValue(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    oauthToken.getAuthorizedClientRegistrationId(),
                    oauthToken.getName());
            OAuth2AccessToken accessToken = client.getAccessToken();
            return accessToken.getTokenValue();
        }
        throw new UnsupportedOperationException(authentication.getClass().getName() + " token value retrieval is not supported yet!");
    }

    public OktaProfile getOktaProfile(Authentication authentication) {
        Map<String, Object> tokenAttributes = getTokenAttributes(authentication);
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

    private Map<String, Object> getTokenAttributes(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User principal = oauthToken.getPrincipal();
            return principal.getAttributes();
        }
        throw new UnsupportedOperationException(authentication.getClass().getName() + " token attributes retrieval is not supported yet!");
    }

    private Map<String, Object> loadUserInfoAttributes(Authentication authentication, Map<String, Object> tokenAttributes) {
        Collection<String> scopes = extractCollection(tokenAttributes, SCOPES_CLAIM);
        if (containsAny(scopes, USER_INFO_SCOPES)) {
            Map<String, String> headers = new HashMap<>();
            headers.put(HttpHeaders.AUTHORIZATION, createAuthorizationHeaderValue(authentication));
            return oktaConnector.getUserInfo(headers);
        }
        return new HashMap<>();
    }

    private String createAuthorizationHeaderValue(Authentication authentication) {
        String tokenValue = getTokenValue(authentication);
        return format("%s %s", BEARER.getValue(), tokenValue);
    }
}
