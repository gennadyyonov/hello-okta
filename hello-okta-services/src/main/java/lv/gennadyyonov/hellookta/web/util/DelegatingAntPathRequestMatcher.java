package lv.gennadyyonov.hellookta.web.util;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class DelegatingAntPathRequestMatcher implements PatternRequestMatcher {

    private final AntPathRequestMatcher delegate;

    @Override
    public String getPattern() {
        return delegate.getPattern();
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return delegate.matches(request);
    }
}
