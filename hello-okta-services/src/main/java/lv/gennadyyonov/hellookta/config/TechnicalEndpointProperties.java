package lv.gennadyyonov.hellookta.config;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@ConfigurationProperties(prefix = "technical-endpoint")
@ConstructorBinding
@Validated
@RequiredArgsConstructor
@Value
public class TechnicalEndpointProperties {

    @NotNull
    Boolean enabled;
    List<String> referrerHeaderNames;
    @Valid
    List<Endpoint> endpoints;

    @Value
    public static class Endpoint {

        @NotNull
        Boolean enabled;
        List<String> allowedClasses;
        List<String> allowedEndpoints;
    }
}
