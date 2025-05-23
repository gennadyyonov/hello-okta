package lv.gennadyyonov.hellookta.config.csrf;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.services.TechnicalEndpointService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static lv.gennadyyonov.hellookta.utils.StreamUtils.getNullableFlatStream;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(CsrfProperties.class)
public class CsrfConfiguration {

  private final CsrfProperties csrfProperties;
  private final TechnicalEndpointService technicalEndpointService;

  /**
   * <b>CSRF protection</b> - ensure backward compatibility with Spring Security 5.8 - <u>Opt-out of
   * Deferred CSRF Tokens</u> (set <code>CsrfTokenRequestAttributeHandler#csrfRequestAttributeName
   * </code> to <code>null</code>)
   *
   * @see <a href="https://github.com/spring-projects/spring-security/issues/13011">Spring Security
   *     6.x / Single Page Web Application / CSRF - formLogin not working anymore</a>
   * @see <a
   *     href="https://docs.spring.io/spring-security/reference/migration/servlet/exploits.html#_defer_loading_csrftoken">Defer
   *     Loading CsrfToken</a>
   */
  @Bean
  public Customizer<CsrfConfigurer<HttpSecurity>> csrfCustomizer(
      CsrfTokenRepository csrfTokenRepository) {
    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    // Opt-out of Deferred CSRF Tokens
    requestHandler.setCsrfRequestAttributeName(null);
    return csrf ->
        csrf.csrfTokenRepository(csrfTokenRepository).csrfTokenRequestHandler(requestHandler);
  }

  @Bean
  public CsrfTokenRepository csrfTokenRepository() {
    CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    csrfTokenRepository.setCookieName(csrfProperties.getCookieName());
    csrfTokenRepository.setHeaderName(csrfProperties.getHeaderName());
    csrfTokenRepository.setCookiePath("/");
    return csrfTokenRepository;
  }

  // https://github.com/spring-projects/spring-security/issues/8668
  @Bean
  public CsrfFilterPostProcessor csrfFilterPostProcessor() {
    return new CsrfFilterPostProcessor(getRequireCsrfProtectionMatcher());
  }

  private RequestMatcher getRequireCsrfProtectionMatcher() {
    List<RequestMatcher> ignores = getIgnoresCsrfProtectionMatchers();
    return new AndRequestMatcher(
        new HttpMethodRequiresCsrfMatcher(new HashSet<>(csrfProperties.getAllowedMethods())),
        new NegatedRequestMatcher(new OrRequestMatcher(ignores)));
  }

  @SuppressWarnings("OptionalOfNullableMisuse")
  private List<RequestMatcher> getIgnoresCsrfProtectionMatchers() {
    List<RequestMatcher> ignores =
        getNullableFlatStream(csrfProperties.getIgnoredEndpoints())
            .map(
                endpoint -> {
                  var method =
                      ofNullable(endpoint.getMethod()).map(HttpMethod::valueOf).orElse(null);
                  return PathPatternRequestMatcher.withDefaults()
                      .matcher(method, endpoint.getPattern());
                })
            .collect(toList());
    ignores.add(technicalEndpointService::isWhitelistedUrl);
    return ignores;
  }

  @RequiredArgsConstructor
  private static final class HttpMethodRequiresCsrfMatcher implements RequestMatcher {

    private final Collection<String> allowedMethods;

    @Override
    public boolean matches(HttpServletRequest request) {
      return !allowedMethods.contains(request.getMethod());
    }

    @Override
    public String toString() {
      return "CsrfNotRequired " + allowedMethods;
    }
  }

  /**
   * This {@link BeanPostProcessor} modifies the {@link CsrfFilter} bean after its initialization in
   * the Spring application context.
   *
   * <p>The main purpose of this post-processor is to customize the behavior of the {@link
   * CsrfFilter} to enforce CSRF protection for all incoming requests, including those that would
   * typically be ignored by the default configuration (e.g., requests with a Bearer token in the
   * Authorization header).
   *
   * <p>By default, Spring Security's {@link
   * OAuth2ResourceServerConfigurer#registerDefaultCsrfOverride} adds a {@link RequestMatcher} that
   * ignores CSRF protection for requests containing a Bearer token (typically OAuth2
   * authentication). This behavior is implemented in {@link OAuth2ResourceServerConfigurer} via the
   * {@link CsrfFilter} and its `requireCsrfProtectionMatcher`, which is configured to bypass CSRF
   * for requests with a Bearer token.
   *
   * <p>In this implementation, the {@link CsrfFilter} is modified to ensure that the CSRF
   * protection is applied universally by setting a custom {@link RequestMatcher} that forces CSRF
   * protection on all requests, including those with a Bearer token.
   *
   * <p>Note: The {@link CsrfFilter} is not normally a Spring Bean, so this post-processor will only
   * work if Spring Security's automatic configuration creates the {@link CsrfFilter} bean in the
   * application context.
   */
  @RequiredArgsConstructor
  public static final class CsrfFilterPostProcessor implements BeanPostProcessor {

    private final RequestMatcher requireCsrfProtectionMatcher;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
        throws BeansException {
      if (bean instanceof CsrfFilter csrfFilter) {
        csrfFilter.setRequireCsrfProtectionMatcher(requireCsrfProtectionMatcher);
      }
      return bean;
    }
  }
}
