package lv.gennadyyonov.hellookta.bff.config.cors;

import lv.gennadyyonov.hellookta.config.csrf.CsrfProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;

@Configuration
@EnableConfigurationProperties(CorsProperties.class)
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsProperties corsProperties,
                                                           CsrfProperties csrfProperties) {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> allowedOrigins = ofNullable(corsProperties.getAllowedOrigins()).orElse(new ArrayList<>());
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(corsProperties.getAllowedMethods());
        configuration.setAllowCredentials(toBoolean(corsProperties.getAllowCredentials()));
        Set<String> allowedHeaders = new HashSet<>(corsProperties.getAllowedHeaders());
        if (toBoolean(csrfProperties.getCsrfEnabled())) {
            allowedHeaders.add(csrfProperties.getHeaderName());
        }
        configuration.setAllowedHeaders(new ArrayList<>(allowedHeaders));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsProperties.getUrlPattern(), configuration);
        return source;
    }
}
