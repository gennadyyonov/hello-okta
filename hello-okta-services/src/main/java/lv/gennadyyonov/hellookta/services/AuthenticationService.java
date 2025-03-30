package lv.gennadyyonov.hellookta.services;

import lv.gennadyyonov.hellookta.dto.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.util.Base64.getEncoder;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;
import static lv.gennadyyonov.hellookta.utils.OktaUtils.extractCollection;
import static lv.gennadyyonov.hellookta.utils.StreamUtils.getNullableFlatStream;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

public class AuthenticationService {

  private static final String GROUPS_CLAIM = "groups";

  public String getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return getUserId(authentication);
  }

  private String getUserId(Authentication authentication) {
    if (authentication instanceof AbstractAuthenticationToken) {
      AbstractAuthenticationToken token = (AbstractAuthenticationToken) authentication;
      return ofNullable(token.getName()).map(String::toUpperCase).orElse(null);
    } else {
      return null;
    }
  }

  public Set<String> getAuthorities() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return getAuthorities(authentication);
  }

  private Set<String> getAuthorities(Authentication authentication) {
    Set<String> authorities =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(toSet());
    Map<String, Object> tokenAttributes = getTokenAttributes(authentication);
    authorities.addAll(extractCollection(tokenAttributes, GROUPS_CLAIM));
    return authorities;
  }

  public String authorizationHeaderValue() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authorizationHeaderValue(authentication);
  }

  private String authorizationHeaderValue(Authentication authentication) {
    String tokenValue = getTokenValue(authentication);
    return format("%s %s", BEARER.getValue(), tokenValue);
  }

  public String bearerTokenValue() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return getTokenValue(authentication);
  }

  public String basicAuthorizationHeaderValue(String username, String password) {
    return "Basic " + getEncoder().encodeToString((username + ":" + password).getBytes(ISO_8859_1));
  }

  private String getTokenValue(Authentication authentication) {
    if (authentication instanceof JwtAuthenticationToken) {
      JwtAuthenticationToken oauthToken = (JwtAuthenticationToken) authentication;
      return oauthToken.getToken().getTokenValue();
    }
    throw new UnsupportedOperationException(
        authentication.getClass().getName() + " token value retrieval is not supported yet!");
  }

  public Map<String, Object> getTokenAttributes() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return getTokenAttributes(authentication);
  }

  private Map<String, Object> getTokenAttributes(Authentication authentication) {
    if (authentication instanceof JwtAuthenticationToken) {
      JwtAuthenticationToken oauthToken = (JwtAuthenticationToken) authentication;
      return oauthToken.getTokenAttributes();
    } else if (authentication instanceof AnonymousAuthenticationToken) {
      return new HashMap<>();
    }
    throw new UnsupportedOperationException(
        authentication.getClass().getName() + " token attributes retrieval is not supported yet!");
  }

  public void initUsers(AuthenticationManagerBuilder auth, List<User> users) throws Exception {
    InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> configurer =
        auth.inMemoryAuthentication();
    getNullableFlatStream(users)
        .forEach(
            user ->
                configurer
                    .withUser(user.getName())
                    .password(user.getPassword())
                    .roles(
                        ofNullable(user.getRoles())
                            .orElseGet(ArrayList::new)
                            .toArray(new String[0])));
  }
}
