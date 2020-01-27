package lv.gennadyyonov.hellookta.bff.config;

import lv.gennadyyonov.hellookta.connectors.UserInfoConnector;
import lv.gennadyyonov.hellookta.services.AuthenticationService;
import lv.gennadyyonov.hellookta.services.SecurityService;
import lv.gennadyyonov.hellookta.services.UserInfoService;
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
    public AuthenticationService authenticationService() {
        return new AuthenticationService(authorizedClientService);
    }

    @Bean
    public UserInfoService userInfoService(AuthenticationService authenticationService,
                                           UserInfoConnector userInfoConnector) {
        return new UserInfoService(authenticationService, issuerUrl, userInfoConnector);
    }

    @Bean
    public SecurityService securityService(AuthenticationService authenticationService,
                                           UserInfoService userInfoService,
                                           HelloOktaBFFProperties helloOktaBFFProperties) {
        return new SecurityService(authenticationService, userInfoService, helloOktaBFFProperties);
    }
}
