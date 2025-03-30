package lv.gennadyyonov.hellookta.web.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.util.AntPathMatcher;

import java.net.MalformedURLException;
import java.net.URL;

import static java.util.Optional.ofNullable;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class RefererHeaderRequestMatcher implements PatternRequestMatcher {

  private final String headerName;
  private final String pattern;

  private final AntPathMatcher matcher = new AntPathMatcher();

  @Override
  public String getPattern() {
    return pattern;
  }

  @Override
  public boolean matches(HttpServletRequest request) {
    return ofNullable(request.getHeader(headerName))
        .map(
            url -> {
              try {
                String path = new URL(url).getPath();
                String contextPath = request.getServletContext().getContextPath();
                return path.startsWith(contextPath) ? path.substring(contextPath.length()) : path;
              } catch (MalformedURLException e) {
                return url;
              }
            })
        .map(header -> matcher.match(pattern, header))
        .orElse(false);
  }
}
