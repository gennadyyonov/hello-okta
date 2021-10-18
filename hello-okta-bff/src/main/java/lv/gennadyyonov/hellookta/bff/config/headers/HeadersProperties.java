package lv.gennadyyonov.hellookta.bff.config.headers;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "headers")
@ConstructorBinding
@Validated
@RequiredArgsConstructor
@Value
public class HeadersProperties {

    @Valid
    Csp csp;

    @Value
    static class Csp {

        @NotBlank
        String directives;
    }
}
