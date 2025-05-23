package lv.gennadyyonov.hellookta.web.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@EqualsAndHashCode
@ToString
public class DelegatingPatternRequestMatcher implements PatternRequestMatcher {

  private final String pattern;
  private final PathPatternRequestMatcher delegate;

  public DelegatingPatternRequestMatcher(String pattern) {
    delegate = PathPatternRequestMatcher.withDefaults().matcher(pattern);
    this.pattern = pattern;
  }

  @Override
  public String getPattern() {
    return pattern;
  }

  @Override
  public boolean matches(HttpServletRequest request) {
    return delegate.matches(request);
  }
}
