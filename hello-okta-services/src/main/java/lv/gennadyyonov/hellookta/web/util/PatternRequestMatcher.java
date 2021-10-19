package lv.gennadyyonov.hellookta.web.util;

import org.springframework.security.web.util.matcher.RequestMatcher;

public interface PatternRequestMatcher extends RequestMatcher {

    String getPattern();
}
