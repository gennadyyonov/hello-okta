package lv.gennadyyonov.hellookta.api.config;

import lv.gennadyyonov.hellookta.dto.SecurityMappingProperties;
import lv.gennadyyonov.hellookta.web.FilterOrderProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OktaConfig {

    @Bean
    public SecurityMappingProperties securityMappingProperties() {
        return new HelloOktaAPIProperties();
    }

    @Bean
    public FilterOrderProperties filterOrderProperties() {
        return new HelloOktaAPIProperties();
    }
}
