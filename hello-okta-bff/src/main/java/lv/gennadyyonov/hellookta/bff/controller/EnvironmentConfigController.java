package lv.gennadyyonov.hellookta.bff.controller;

import com.okta.spring.boot.oauth.config.OktaOAuth2Properties;
import lv.gennadyyonov.hellookta.aspects.HasRole;
import lv.gennadyyonov.hellookta.bff.dto.EnvironmentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Optional.ofNullable;
import static lv.gennadyyonov.hellookta.constants.SecurityConstants.PUBLIC_ENDPOINT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@HasRole(alias = PUBLIC_ENDPOINT)
@RestController
public class EnvironmentConfigController {

    public static final String ENVIRONMENT_CONFIG_SUFFIX = "/config/environment";

    private final OktaOAuth2Properties oktaOAuth2Properties;

    @Autowired
    public EnvironmentConfigController(OktaOAuth2Properties oktaOAuth2Properties) {
        this.oktaOAuth2Properties = oktaOAuth2Properties;
    }

    @GetMapping(value = ENVIRONMENT_CONFIG_SUFFIX, produces = APPLICATION_JSON_VALUE)
    public EnvironmentProperties environmentConfig() {
        return EnvironmentProperties.builder()
                .oktaClientId(ofNullable(oktaOAuth2Properties).map(OktaOAuth2Properties::getClientId).orElse(null))
                .oktaIssuer(ofNullable(oktaOAuth2Properties).map(OktaOAuth2Properties::getIssuer).orElse(null))
                .build();
    }
}
