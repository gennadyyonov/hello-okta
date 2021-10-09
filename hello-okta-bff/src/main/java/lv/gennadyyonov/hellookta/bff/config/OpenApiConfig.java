package lv.gennadyyonov.hellookta.bff.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Collections.singletonList;
import static lv.gennadyyonov.hellookta.utils.OktaUtils.getIssuerUri;

@Configuration
public class OpenApiConfig {

    private static final String SPRING_OAUTH = "spring_oauth";

    @Bean
    public OpenAPI api(OAuth2ClientProperties oktaOAuth2Properties) {
        String issuer = getIssuerUri(oktaOAuth2Properties);
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(SPRING_OAUTH, oauth2Flow(issuer)))
                .security(singletonList(new SecurityRequirement().addList(SPRING_OAUTH)))
                .info(info());
    }

    private SecurityScheme oauth2Flow(String issuer) {
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .description("OAuth2 Flow")
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl(issuer + "/v1/authorize")
                                .tokenUrl(issuer + "/v1/token")
                                .scopes(new Scopes())));
    }

    private Info info() {
        return new Info()
                .title("Hello Okta API")
                .version("v1.0");
    }
}
