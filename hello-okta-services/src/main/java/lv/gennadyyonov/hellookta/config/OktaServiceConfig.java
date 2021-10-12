package lv.gennadyyonov.hellookta.config;

import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.aspects.SecurityRoleAspect;
import lv.gennadyyonov.hellookta.config.feign.FeignInterceptorProvider;
import lv.gennadyyonov.hellookta.connectors.TokenConnector;
import lv.gennadyyonov.hellookta.connectors.UserInfoConnector;
import lv.gennadyyonov.hellookta.dto.SecurityMappingProperties;
import lv.gennadyyonov.hellookta.services.AuthenticationService;
import lv.gennadyyonov.hellookta.services.SecurityService;
import lv.gennadyyonov.hellookta.services.TechnicalEndpointService;
import lv.gennadyyonov.hellookta.services.TokenService;
import lv.gennadyyonov.hellookta.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static lv.gennadyyonov.hellookta.utils.OktaUtils.getIssuerUri;

@EnableConfigurationProperties(OAuth2ClientProperties.class)
@RequiredArgsConstructor
@Configuration
public class OktaServiceConfig {

    private final OAuth2ClientProperties oktaOAuth2Properties;

    @Bean
    public AuthenticationService authenticationService() {
        return new AuthenticationService();
    }

    @Bean
    public UserInfoService userInfoService(AuthenticationService authenticationService,
                                           @Lazy UserInfoConnector userInfoConnector) {
        String issuerUrl = getIssuerUri(oktaOAuth2Properties);
        return new UserInfoService(authenticationService, issuerUrl, userInfoConnector);
    }

    @Bean
    public SecurityService securityService(AuthenticationService authenticationService,
                                           UserInfoService userInfoService,
                                           SecurityMappingProperties securityMappingProperties) {
        return new SecurityService(authenticationService, userInfoService, securityMappingProperties);
    }

    @Bean
    public TokenService tokenService(@Lazy TokenConnector tokenConnector, AuthenticationService authenticationService) {
        return new TokenService(tokenConnector, authenticationService);
    }

    @Bean
    public FeignInterceptorProvider feignInterceptorProvider(AuthenticationService authenticationService,
                                                             TokenService tokenService) {
        return new FeignInterceptorProvider(authenticationService, tokenService);
    }

    @Bean
    public SecurityRoleAspect securityRoleAspect(AuthenticationService authenticationService, SecurityService securityService,
                                                 @Autowired(required = false) TechnicalEndpointService technicalEndpointService) {
        return new SecurityRoleAspect(authenticationService, securityService, technicalEndpointService);
    }
}
