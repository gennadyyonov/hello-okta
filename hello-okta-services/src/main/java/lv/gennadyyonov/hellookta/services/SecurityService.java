package lv.gennadyyonov.hellookta.services;

import lv.gennadyyonov.hellookta.dto.OktaProfile;
import lv.gennadyyonov.hellookta.dto.UserInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.util.Base64.getEncoder;

public class SecurityService {

    private final OktaService oktaService;

    public SecurityService(OktaService oktaService) {
        this.oktaService = oktaService;
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
}
