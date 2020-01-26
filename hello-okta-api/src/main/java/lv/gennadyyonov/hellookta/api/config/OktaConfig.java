package lv.gennadyyonov.hellookta.api.config;

import lv.gennadyyonov.hellookta.connectors.OktaConnector;
import lv.gennadyyonov.hellookta.services.OktaService;
import lv.gennadyyonov.hellookta.services.SecurityService;
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
        return new OktaConnector(issuerUrl);
    }

    @Bean
    public OktaService oktaService(OktaConnector oktaConnector) {
        return new OktaService(authorizedClientService, oktaConnector);
    }

    @Bean
    public SecurityService securityService(OktaService oktaService, HelloOktaAPIProperties helloOktaAPIProperties) {
        return new SecurityService(oktaService, helloOktaAPIProperties);
    }
}
