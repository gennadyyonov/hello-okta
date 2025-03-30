package lv.gennadyyonov.hellookta.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lv.gennadyyonov.hellookta.config.TechnicalEndpointProperties;
import lv.gennadyyonov.hellookta.web.util.DelegatingAntPathRequestMatcher;
import lv.gennadyyonov.hellookta.web.util.PatternRequestMatcher;
import lv.gennadyyonov.hellookta.web.util.RefererHeaderRequestMatcher;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;
import static lv.gennadyyonov.hellookta.utils.StreamUtils.getNullableFlatStream;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;

@Slf4j
public class TechnicalEndpointService {

  private final boolean technicalEndpointEnabled;
  private final Collection<String> additionalRealEndpoints;
  private final Collection<Class<?>> allowedClasses;
  private final Collection<PatternRequestMatcher> allowedRequestMatchers;

  public TechnicalEndpointService(TechnicalEndpointProperties props) {
    technicalEndpointEnabled = toBoolean(props.getEnabled());
    additionalRealEndpoints =
        technicalEndpointEnabled ? getAdditionalRealEndpoints(props) : emptyList();
    allowedClasses = technicalEndpointEnabled ? getAllowedClasses(props) : emptySet();
    allowedRequestMatchers =
        technicalEndpointEnabled ? getAllowedRequestMatchers(props) : emptySet();
  }

  private Collection<String> getAdditionalRealEndpoints(TechnicalEndpointProperties props) {
    return ofNullable(props.getAdditionalRealEndpoints()).orElse(emptyList());
  }

  private Collection<Class<?>> getAllowedClasses(TechnicalEndpointProperties props) {
    return getEnabledEndpointFlatStream(
            props, TechnicalEndpointProperties.Endpoint::getAllowedClasses)
        .map(
            clazz -> {
              try {
                return Class.forName(clazz);
              } catch (ClassNotFoundException ex) {
                throw new IllegalArgumentException(ex);
              }
            })
        .collect(toSet());
  }

  private Collection<PatternRequestMatcher> getAllowedRequestMatchers(
      TechnicalEndpointProperties props) {
    Collection<String> endpoints =
        getEnabledEndpointFlatStream(
                props, TechnicalEndpointProperties.Endpoint::getAllowedEndpoints)
            .collect(toSet());
    Collection<PatternRequestMatcher> allMatchers =
        endpoints.stream()
            .map(AntPathRequestMatcher::new)
            .map(DelegatingAntPathRequestMatcher::new)
            .collect(toSet());
    getNullableFlatStream(props.getReferrerHeaderNames())
        .forEach(
            headerName ->
                endpoints.forEach(
                    endpoint ->
                        allMatchers.add(new RefererHeaderRequestMatcher(headerName, endpoint))));
    return allMatchers;
  }

  private <T> Stream<T> getEnabledEndpointFlatStream(
      TechnicalEndpointProperties techEndpointProps,
      Function<TechnicalEndpointProperties.Endpoint, List<T>> func) {
    return getNullableFlatStream(techEndpointProps.getEndpoints())
        .filter(endpoint -> toBoolean(endpoint.getEnabled()))
        .map(func)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream);
  }

  public boolean isAllowed(ProceedingJoinPoint joinPoint) {
    if (!technicalEndpointEnabled) {
      return false;
    }
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Class<?> clazz = signature.getMethod().getDeclaringClass();
    return allowedClasses.stream().anyMatch(allowedClass -> allowedClass.isAssignableFrom(clazz));
  }

  @SneakyThrows
  public void allowTechnicalEndpoints(HttpSecurity http) {
    if (!technicalEndpointEnabled) {
      return;
    }
    Collection<String> allowedPatterns =
        allowedRequestMatchers.stream().map(PatternRequestMatcher::getPattern).collect(toSet());
    allowedPatterns.addAll(additionalRealEndpoints);
    if (!allowedPatterns.isEmpty()) {
      RequestMatcher[] matchers =
          allowedPatterns.stream()
              .map(AntPathRequestMatcher::antMatcher)
              .toArray(AntPathRequestMatcher[]::new);
      http.authorizeHttpRequests(auth -> auth.requestMatchers(matchers).permitAll());
    }
  }

  public boolean isWhitelistedUrl(HttpServletRequest request) {
    if (!technicalEndpointEnabled) {
      return false;
    }
    return getNullableFlatStream(allowedRequestMatchers)
        .anyMatch(matcher -> matcher.matches(request));
  }
}
