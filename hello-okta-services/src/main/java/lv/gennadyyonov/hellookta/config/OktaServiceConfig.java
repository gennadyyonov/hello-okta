package lv.gennadyyonov.hellookta.config;

import feign.Client;
import feign.Target;
import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.aspects.SecurityRoleAspect;
import lv.gennadyyonov.hellookta.config.feign.FeignClientProvider;
import lv.gennadyyonov.hellookta.config.feign.SsoInterceptor;
import lv.gennadyyonov.hellookta.connectors.TokenConnector;
import lv.gennadyyonov.hellookta.connectors.UserInfoConnector;
import lv.gennadyyonov.hellookta.dto.SecurityMappingProperties;
import lv.gennadyyonov.hellookta.services.AuthenticationService;
import lv.gennadyyonov.hellookta.services.SecurityService;
import lv.gennadyyonov.hellookta.services.TokenService;
import lv.gennadyyonov.hellookta.services.UserInfoService;
import lv.gennadyyonov.hellookta.utils.FeignUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Configuration
public class OktaServiceConfig {

    private static final String OKTA = "okta";

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final OAuth2ClientProperties oktaOAuth2Properties;

    @Bean
    public UserInfoConnector userInfoConnector(FeignClientProvider feignClientProvider) {
        Client client = feignClientProvider.getClient();
        return FeignUtils.feignBuilder(client, UserInfoConnector.class)
                .target(Target.EmptyTarget.create(UserInfoConnector.class));
    }

    @Bean
    public TokenConnector tokenConnector(FeignClientProvider feignClientProvider) {
        Client client = feignClientProvider.getClient();
        return FeignUtils.feignBuilder(client, TokenConnector.class, FeignUtils.feignFormEncoder())
                .target(Target.EmptyTarget.create(TokenConnector.class));
    }

    @Bean
    public AuthenticationService authenticationService() {
        return new AuthenticationService(authorizedClientService);
    }

    @Bean
    @ConditionalOnBean({UserInfoConnector.class})
    public UserInfoService userInfoService(AuthenticationService authenticationService,
                                           UserInfoConnector userInfoConnector) {
        String issuerUrl = ofNullable(oktaOAuth2Properties)
                .map(OAuth2ClientProperties::getProvider)
                .map(map -> map.get(OKTA))
                .map(OAuth2ClientProperties.Provider::getIssuerUri)
                .orElse(null);
        return new UserInfoService(authenticationService, issuerUrl, userInfoConnector);
    }

    @Bean
    public SecurityService securityService(AuthenticationService authenticationService,
                                           UserInfoService userInfoService,
                                           SecurityMappingProperties securityMappingProperties) {
        return new SecurityService(authenticationService, userInfoService, securityMappingProperties);
    }

    @Bean
    @ConditionalOnBean({TokenConnector.class})
    public TokenService tokenService(TokenConnector tokenConnector,
                                     AuthenticationService authenticationService) {
        return new TokenService(tokenConnector, authenticationService);
    }

    @Bean
    public SsoInterceptor ssoInterceptor(AuthenticationService authenticationService) {
        return new SsoInterceptor(authenticationService);
    }

    @Bean
    public SecurityRoleAspect securityRoleAspect(AuthenticationService authenticationService,
                                                 SecurityService securityService) {
        return new SecurityRoleAspect(authenticationService, securityService);
    }
}
