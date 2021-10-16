package lv.gennadyyonov.hellookta.bff.config;

import lombok.RequiredArgsConstructor;
import lombok.Value;
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
@Value
public class HelloOktaBffProps implements SecurityMappingProperties, FilterOrderProperties {

    @NotNull
    String chuckNorrisUrl;
    @NotNull
    Map<String, Set<String>> securityMapping;
    @NotNull
    Map<String, Integer> filterOrderMapping;
}
