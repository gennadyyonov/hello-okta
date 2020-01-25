package lv.gennadyyonov.hellookta.bff.config;

import lv.gennadyyonov.hellookta.services.OktaConnector;
import lv.gennadyyonov.hellookta.services.OktaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

@Configuration
public class OktaConfig {

    @Value("${okta.oauth2.issuer}")
    private String issuerUrl;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Bean
    public OktaConnector oktaConnector() {
        return new OktaConnector(issuerUrl + "/v1/userinfo");
    }

    @Bean
    public OktaService oktaService(OktaConnector oktaConnector) {
        return new OktaService(authorizedClientService, oktaConnector);
    }
}
