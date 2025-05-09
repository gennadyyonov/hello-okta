package lv.gennadyyonov.hellookta.api.config;

import lv.gennadyyonov.hellookta.dto.FilterOrderProperties;
import lv.gennadyyonov.hellookta.dto.SecurityMappingProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OktaConfig {

  @Bean
  public SecurityMappingProperties securityMappingProperties() {
    return new HelloOktaApiProps();
  }

  @Bean
  public FilterOrderProperties filterOrderProperties() {
    return new HelloOktaApiProps();
  }
}
