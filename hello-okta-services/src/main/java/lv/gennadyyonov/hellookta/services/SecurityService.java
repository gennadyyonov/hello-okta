package lv.gennadyyonov.hellookta.services;

import lombok.extern.slf4j.Slf4j;
import lv.gennadyyonov.hellookta.dto.SecurityMappingProperties;
import lv.gennadyyonov.hellookta.dto.UserInfo;
import lv.gennadyyonov.hellookta.utils.SecurityUtils;

import java.util.Arrays;
import java.util.Set;

import static java.util.Optional.ofNullable;

@Slf4j
public class SecurityService {

    private final AuthenticationService authenticationService;
    private final UserInfoService userInfoService;
    private final SecurityMappingProperties securityMappingProperties;

    public SecurityService(AuthenticationService authenticationService,
                           UserInfoService userInfoService,
                           SecurityMappingProperties securityMappingProperties) {
        this.authenticationService = authenticationService;
        this.userInfoService = userInfoService;
        this.securityMappingProperties = securityMappingProperties;
    }

    public boolean hasAnyRoles(String alias, String[] roles) {
        if (SecurityUtils.shouldBypassSecurityValidation(alias)) {
            return true;
        }
        String userId = authenticationService.getUserId();
        UserInfo userInfo = userInfoService.getUserInfo();
        if (userInfo == null) {
            log.warn("No User profile found for User ID  : {}", userId);
            return false;
        }
        log.info("Checking hasAnyRoles for User ID : {}, Alias : {}, Roles: {}", userId, alias, Arrays.toString(roles));
        if (!ofNullable(userInfo.getRoles()).isPresent()) {
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
