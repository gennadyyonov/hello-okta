package lv.gennadyyonov.hellookta.config;

import lv.gennadyyonov.hellookta.services.TechnicalEndpointService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TechnicalEndpointProperties.class)
public class TechnicalEndpointConfig {

    @Bean
    public TechnicalEndpointService technicalEndpointService(TechnicalEndpointProperties technicalEndpointProperties) {
        return new TechnicalEndpointService(technicalEndpointProperties);
    }
}
