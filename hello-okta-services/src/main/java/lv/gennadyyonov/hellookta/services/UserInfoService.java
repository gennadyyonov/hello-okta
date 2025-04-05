package lv.gennadyyonov.hellookta.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lv.gennadyyonov.hellookta.connectors.UserInfoConnector;
import lv.gennadyyonov.hellookta.dto.UserInfo;
import lv.gennadyyonov.hellookta.exception.ExternalSystemException;
import org.springframework.security.oauth2.core.oidc.StandardClaimAccessor;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

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
  private static final Collection<String> USER_INFO_SCOPES = Set.of(PROFILE, EMAIL, ADDRESS, PHONE);

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
    Map<String, Object> attributes = getUserInfoAttributes();
    Set<String> authorities = authenticationService.getAuthorities();
    log.debug("Authorities: {}", authorities);
    return new OktaProfile(attributes, authorities);
  }

  private Map<String, Object> getUserInfoAttributes() {
    Map<String, Object> tokenAttributes = authenticationService.getTokenAttributes();
    log.debug("User attributes from authentication token: {}", tokenAttributes);
    if (hasRequiredClaims(tokenAttributes)) {
      return tokenAttributes;
    }
    Map<String, Object> userInfoAttributes = loadUserInfoAttributes(tokenAttributes);
    log.debug("User Info attributes from Okta: {}", userInfoAttributes);
    Map<String, Object> attributes = new HashMap<>();
    attributes.putAll(tokenAttributes);
    attributes.putAll(userInfoAttributes);
    return attributes;
  }

  private boolean hasRequiredClaims(Map<String, Object> tokenAttributes) {
    return Stream.of(
            StandardClaimNames.GIVEN_NAME, StandardClaimNames.FAMILY_NAME, StandardClaimNames.EMAIL)
        .allMatch(claim -> tokenAttributes.getOrDefault(claim, null) != null);
  }

  @SneakyThrows
  private Map<String, Object> loadUserInfoAttributes(Map<String, Object> tokenAttributes) {
    Collection<String> scopes = extractCollection(tokenAttributes, SCOPES_CLAIM);
    if (containsAny(scopes, USER_INFO_SCOPES)) {
      try {
        Map<String, Object> headers =
            Map.of(AUTHORIZATION, authenticationService.authorizationHeaderValue());
        return userInfoConnector.getUserInfo(headers);
      } catch (Exception e) {
        log.error("Error fetching user info from Okta", e);
        throw new ExternalSystemException("Failed to fetch user info from Okta", e);
      }
    }
    return Collections.emptyMap();
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
