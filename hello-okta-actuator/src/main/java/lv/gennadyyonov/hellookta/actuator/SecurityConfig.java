package lv.gennadyyonov.hellookta.actuator;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;
import static java.util.Optional.ofNullable;

// https://docs.spring.io/spring-security/reference/servlet/configuration/java.html#_multiple_httpsecurity_instances
@RequiredArgsConstructor
@EnableConfigurationProperties({ProxyProperties.class, SecurityProperties.class})
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

  private final ProxyProperties proxyProperties;
  private final SecurityProperties securityProperties;

  @Bean
  @Order(50)
  protected SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .securityMatcher(new OrRequestMatcher(getRequestMatchers()));
    configureAuthentication(http);
    return http.build();
  }

  @SneakyThrows
  private void configureAuthentication(HttpSecurity http) {
    if (TRUE.equals(securityProperties.getEnabled())) {
      String[] allowedRoles =
          ofNullable(securityProperties.getAllowedRoles())
              .map(roles -> roles.toArray(new String[0]))
              .orElseThrow(() -> new IllegalArgumentException("Allowed roles MUST NOT be null!"));
      http.authorizeHttpRequests(auth -> auth.anyRequest().hasAnyRole(allowedRoles))
          .httpBasic(Customizer.withDefaults());
    } else {
      http.authorizeHttpRequests(requests -> requests.anyRequest().permitAll());
    }
  }

  private List<RequestMatcher> getRequestMatchers() {
    List<RequestMatcher> requestMatchers = new ArrayList<>();
    requestMatchers.add(EndpointRequest.toAnyEndpoint());
    if (TRUE.equals(proxyProperties.getEnabled())) {
      requestMatchers.add(
          PathPatternRequestMatcher.withDefaults().matcher(proxyProperties.getPath() + "/**"));
    }
    return requestMatchers;
  }
}
