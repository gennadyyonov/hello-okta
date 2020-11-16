package lv.gennadyyonov.hellookta.bff.config;

import lv.gennadyyonov.hellookta.dto.FilterOrderProperties;
import lv.gennadyyonov.hellookta.dto.SecurityMappingProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OktaConfig {

    static final String HELLO_OKTA_BFF_PROPS_BEAN_NAME = "helloOktaBffProps";

    @Bean
    public SecurityMappingProperties securityMappingProperties() {
        return new HelloOktaBffProps();
    }

    @Bean
    public FilterOrderProperties filterOrderProperties() {
        return new HelloOktaBffProps();
    }

    @Bean(HELLO_OKTA_BFF_PROPS_BEAN_NAME)
    public HelloOktaBffProps helloOktaBffProps() {
        return new HelloOktaBffProps();
    }
}
