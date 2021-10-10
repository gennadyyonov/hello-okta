package lv.gennadyyonov.hellookta.bff.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.dto.FilterOrderProperties;
import lv.gennadyyonov.hellookta.dto.SecurityMappingProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

@ConfigurationProperties(prefix = "hello-okta-bff")
@RequiredArgsConstructor
@ConstructorBinding
@Validated
@Getter
public class HelloOktaBffProps implements SecurityMappingProperties, FilterOrderProperties {

    private final String allowedOrigins;
    @NotNull
    private final String chuckNorrisUrl;
    @NotNull
    private final Map<String, Set<String>> securityMapping;
    @NotNull
    private final Map<String, Integer> filterOrderMapping;
}
