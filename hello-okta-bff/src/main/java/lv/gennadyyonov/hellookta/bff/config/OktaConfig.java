package lv.gennadyyonov.hellookta.bff.config;

import lv.gennadyyonov.hellookta.dto.SecurityMappingProperties;
import lv.gennadyyonov.hellookta.web.FilterOrderProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OktaConfig {

    @Bean
    public SecurityMappingProperties securityMappingProperties() {
        return new HelloOktaBFFProperties();
    }

    @Bean
    public FilterOrderProperties filterOrderProperties() {
        return new HelloOktaBFFProperties();
    }
}
