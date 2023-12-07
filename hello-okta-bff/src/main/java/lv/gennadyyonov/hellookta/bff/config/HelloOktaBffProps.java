package lv.gennadyyonov.hellookta.bff.config;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lv.gennadyyonov.hellookta.dto.FilterOrderProperties;
import lv.gennadyyonov.hellookta.dto.SecurityMappingProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.Set;

@ConfigurationProperties(prefix = "hello-okta-bff")
@RequiredArgsConstructor
@Validated
@Value
public class HelloOktaBffProps implements SecurityMappingProperties, FilterOrderProperties {

    @NotNull
    Map<String, Set<String>> securityMapping;
    @NotNull
    Map<String, Integer> filterOrderMapping;
}
