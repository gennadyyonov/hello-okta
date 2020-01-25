package lv.gennadyyonov.hellookta.bff.services;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.util.Base64.getEncoder;

@Service
public class SecurityService {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public SecurityService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AbstractAuthenticationToken) {
            AbstractAuthenticationToken token = (AbstractAuthenticationToken) authentication;
            return token.getName();
        } else {
            return null;
        }
    }

    public String bearerTokenValue() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());
        OAuth2AccessToken accessToken = client.getAccessToken();
        return accessToken.getTokenValue();
    }

    public String basicAuthorizationHeaderValue(BaseOAuth2ProtectedResourceDetails resourceDetails) {
        String username = resourceDetails.getClientId();
        String password = resourceDetails.getClientSecret();
        return basicAuthorizationHeaderValue(username, password);
    }

    public String basicAuthorizationHeaderValue(String username, String password) {
        return "Basic " + getEncoder().encodeToString((username + ":" + password).getBytes(ISO_8859_1));
    }
}
