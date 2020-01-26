package lv.gennadyyonov.hellookta.services;

import lombok.extern.slf4j.Slf4j;
import lv.gennadyyonov.hellookta.dto.OktaProfile;
import lv.gennadyyonov.hellookta.dto.SecurityMappingProperties;
import lv.gennadyyonov.hellookta.dto.UserInfo;
import lv.gennadyyonov.hellookta.utils.SecurityUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Set;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.util.Base64.getEncoder;
import static java.util.Optional.ofNullable;

@Slf4j
public class SecurityService {

    private final OktaService oktaService;
    private final SecurityMappingProperties securityMappingProperties;

    public SecurityService(OktaService oktaService, SecurityMappingProperties securityMappingProperties) {
        this.oktaService = oktaService;
        this.securityMappingProperties = securityMappingProperties;
    }

    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getUserId(authentication);
    }

    public UserInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OktaProfile oktaProfile = oktaService.getOktaProfile(authentication);
        return UserInfo.builder()
                .userId(getUserId(authentication))
                .firstName(oktaProfile.getGivenName())
                .lastName(oktaProfile.getFamilyName())
                .email(oktaProfile.getEmail())
                .roles(oktaProfile.getAuthorities())
                .build();
    }

    private String getUserId(Authentication authentication) {
        if (authentication instanceof AbstractAuthenticationToken) {
            AbstractAuthenticationToken token = (AbstractAuthenticationToken) authentication;
            return token.getName();
        } else {
            return null;
        }
    }

    public String bearerTokenValue() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return oktaService.getTokenValue(authentication);
    }

    public String basicAuthorizationHeaderValue(String username, String password) {
        return "Basic " + getEncoder().encodeToString((username + ":" + password).getBytes(ISO_8859_1));
    }

    public boolean hasAnyRoles(String alias, String[] roles) {
        String userId = getUserId();
        UserInfo userInfo = getUserInfo();
        if (userInfo == null) {
            log.warn("No User profile found for User ID  : {}", userId);
            return false;
        }
        log.info("Checking hasAnyRoles for User ID : {}, Alias : {}, Roles: {}", userId, alias, Arrays.toString(roles));
        if (ofNullable(userInfo.getRoles()).isEmpty()) {
            log.warn("No roles found for User ID: {}", userId);
            return false;
        }
        if (SecurityUtils.hasAnyRoles(userInfo, roles)) {
            log.info("Found role in roles: " + Arrays.toString(roles));
            return true;
        }
        Set<String> aliasRoles = securityMappingProperties.getSecurityMappingSetValue(alias);
        log.info("Try to find User Roles by Alias: {} in Roles: {}", alias, aliasRoles);
        boolean match = SecurityUtils.hasAnyRoles(userInfo, aliasRoles.stream());
        log.info("hasAnyRoles by User ID: {} returned: {}", userId, match);
        return match;
    }
}
