package lv.gennadyyonov.hellookta.bff.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Collections.singletonList;
import static lv.gennadyyonov.hellookta.utils.OktaUtils.getIssuerUri;

@Configuration
public class OpenApiConfig {

  private static final String GROUP_NAME = "internal";
  private static final String DISPLAY_NAME = "Internal API";
  private static final String SPRING_OAUTH = "spring_oauth";

  @Bean
  public OpenAPI api() {
    return new OpenAPI().info(info());
  }

  @Bean
  public GroupedOpenApi internalApi(OAuth2ClientProperties oktaOAuth2Properties) {
    return GroupedOpenApi.builder()
        .group(GROUP_NAME)
        .displayName(DISPLAY_NAME)
        .packagesToExclude("lv.gennadyyonov.hellookta.actuator")
        .addOpenApiCustomizer(securityCustomizer(oktaOAuth2Properties))
        .build();
  }

  private OpenApiCustomizer securityCustomizer(OAuth2ClientProperties oktaOAuth2Properties) {
    return openApi -> {
      String issuer = getIssuerUri(oktaOAuth2Properties);
      openApi
          .components(new Components().addSecuritySchemes(SPRING_OAUTH, oauth2Flow(issuer)))
          .security(singletonList(new SecurityRequirement().addList(SPRING_OAUTH)));
    };
  }

  private SecurityScheme oauth2Flow(String issuer) {
    return new SecurityScheme()
        .type(SecurityScheme.Type.OAUTH2)
        .description("OAuth2 Flow")
        .flows(
            new OAuthFlows()
                .authorizationCode(
                    new OAuthFlow()
                        .authorizationUrl(issuer + "/v1/authorize")
                        .tokenUrl(issuer + "/v1/token")
                        .scopes(new Scopes())));
  }

  private Info info() {
    return new Info().title("Hello Okta API").version("v1.0");
  }
}
