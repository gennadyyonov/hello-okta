package lv.gennadyyonov.hellookta.bff.config.headers;

import lv.gennadyyonov.hellookta.services.TechnicalEndpointHeaderWriter;
import lv.gennadyyonov.hellookta.services.TechnicalEndpointService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.header.writers.ContentSecurityPolicyHeaderWriter;

import static org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN;

@Configuration
@EnableConfigurationProperties(HeadersProperties.class)
public class HeadersConfig {

    @Bean
    public Customizer<HeadersConfigurer<HttpSecurity>> headersCustomizer(TechnicalEndpointService technicalEndpointService,
                                                                         HeadersProperties props) {
        return headers -> {
            ContentSecurityPolicyHeaderWriter cspHeaderWriter = new ContentSecurityPolicyHeaderWriter();
            HeadersProperties.Csp csp = props.getCsp();
            cspHeaderWriter.setPolicyDirectives(csp.getDirectives());
            TechnicalEndpointHeaderWriter headerWriter = new TechnicalEndpointHeaderWriter(
                technicalEndpointService, cspHeaderWriter
            );
            headers.addHeaderWriter(headerWriter);
            headers.referrerPolicy(SAME_ORIGIN);
        };
    }
}
