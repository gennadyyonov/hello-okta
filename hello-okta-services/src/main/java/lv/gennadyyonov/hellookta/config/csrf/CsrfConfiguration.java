package lv.gennadyyonov.hellookta.config.csrf;

import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.services.TechnicalEndpointService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lv.gennadyyonov.hellookta.utils.StreamUtils.getNullableFlatStream;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(CsrfProperties.class)
public class CsrfConfiguration {

    private final CsrfProperties csrfProperties;
    private final TechnicalEndpointService technicalEndpointService;

    @Bean
    public Customizer<CsrfConfigurer<HttpSecurity>> csrfCustomizer(CsrfTokenRepository csrfTokenRepository) {
        return csrf -> csrf.csrfTokenRepository(csrfTokenRepository)
            // https://github.com/spring-projects/spring-security/issues/8668
            .withObjectPostProcessor(new ObjectPostProcessor<CsrfFilter>() {
                @Override
                public <T extends CsrfFilter> T postProcess(T object) {
                    object.setRequireCsrfProtectionMatcher(getRequireCsrfProtectionMatcher());
                    return object;
                }
            });
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookieName(csrfProperties.getCookieName());
        csrfTokenRepository.setHeaderName(csrfProperties.getHeaderName());
        csrfTokenRepository.setCookiePath("/");
        return csrfTokenRepository;
    }

    private RequestMatcher getRequireCsrfProtectionMatcher() {
        List<RequestMatcher> ignores = getIgnoresCsrfProtectionMatchers();
        return new AndRequestMatcher(
            new HttpMethodRequiresCsrfMatcher(new HashSet<>(csrfProperties.getAllowedMethods())),
            new NegatedRequestMatcher(new OrRequestMatcher(ignores))
        );
    }

    private List<RequestMatcher> getIgnoresCsrfProtectionMatchers() {
        List<RequestMatcher> ignores = getNullableFlatStream(csrfProperties.getIgnoredEndpoints())
            .map(endpoint -> new AntPathRequestMatcher(endpoint.getPattern(), endpoint.getMethod()))
            .collect(toList());
        List<String> ignoredTechEndpoints = csrfProperties.getIgnoredTechEndpoints();
        ignores.add(request -> technicalEndpointService.isWhitelistedUrl(request, ignoredTechEndpoints));
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
}
