package lv.gennadyyonov.hellookta.bff.controller;

import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.aspects.HasRole;
import lv.gennadyyonov.hellookta.bff.dto.EnvironmentProperties;
import lv.gennadyyonov.hellookta.config.csrf.CsrfProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static lv.gennadyyonov.hellookta.constants.SecurityConstants.PUBLIC_ENDPOINT;
import static lv.gennadyyonov.hellookta.utils.OktaUtils.getClientId;
import static lv.gennadyyonov.hellookta.utils.OktaUtils.getIssuerUri;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@HasRole(alias = PUBLIC_ENDPOINT)
@RestController
public class EnvironmentConfigController {

    public static final String ENVIRONMENT_CONFIG_SUFFIX = "/config/environment";

    private final OAuth2ClientProperties oktaOAuth2Properties;
    private final CsrfProperties csrfProperties;

    @GetMapping(value = ENVIRONMENT_CONFIG_SUFFIX, produces = APPLICATION_JSON_VALUE)
    public EnvironmentProperties environmentConfig() {
        return EnvironmentProperties.builder()
            .oktaClientId(getClientId(oktaOAuth2Properties))
            .oktaIssuer(getIssuerUri(oktaOAuth2Properties))
            .csrfEnabled(csrfProperties.getCsrfEnabled())
            .build();
    }
}
