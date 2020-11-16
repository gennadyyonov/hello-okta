package lv.gennadyyonov.hellookta.bff.controller;

import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.aspects.HasRole;
import lv.gennadyyonov.hellookta.bff.dto.EnvironmentProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Optional.ofNullable;
import static lv.gennadyyonov.hellookta.constants.SecurityConstants.PUBLIC_ENDPOINT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@HasRole(alias = PUBLIC_ENDPOINT)
@RestController
public class EnvironmentConfigController {

    private static final String OKTA = "okta";

    public static final String ENVIRONMENT_CONFIG_SUFFIX = "/config/environment";

    private final OAuth2ClientProperties oktaOAuth2Properties;

    @GetMapping(value = ENVIRONMENT_CONFIG_SUFFIX, produces = APPLICATION_JSON_VALUE)
    public EnvironmentProperties environmentConfig() {
        return EnvironmentProperties.builder()
                .oktaClientId(ofNullable(oktaOAuth2Properties)
                        .map(OAuth2ClientProperties::getRegistration)
                        .map(map -> map.get(OKTA))
                        .map(OAuth2ClientProperties.Registration::getClientId)
                        .orElse(null))
                .oktaIssuer(ofNullable(oktaOAuth2Properties)
                        .map(OAuth2ClientProperties::getProvider)
                        .map(map -> map.get(OKTA))
                        .map(OAuth2ClientProperties.Provider::getIssuerUri)
                        .orElse(null))
                .build();
    }
}
