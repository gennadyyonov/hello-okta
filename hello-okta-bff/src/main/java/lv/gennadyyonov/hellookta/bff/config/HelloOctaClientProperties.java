package lv.gennadyyonov.hellookta.bff.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@ConfigurationProperties("hellooktaclient")
public class HelloOctaClientProperties {

    private String baseUrl;

    private ClientCredentialsResourceDetails clientCredentialsResourceDetails;
}
