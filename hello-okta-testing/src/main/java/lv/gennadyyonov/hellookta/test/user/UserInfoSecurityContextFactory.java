package lv.gennadyyonov.hellookta.test.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserInfoSecurityContextFactory implements WithSecurityContextFactory<UserInfo> {

  @Override
  public SecurityContext createSecurityContext(UserInfo userInfo) {
    Jwt jwt = JwtToken.create(userInfo.username(), Arrays.asList(userInfo.groups()));
    List<GrantedAuthority> grantedAuthorities =
        Stream.of(userInfo.groups()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    Authentication authentication =
        new JwtAuthenticationToken(jwt, grantedAuthorities, userInfo.username());
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    return context;
  }
}
