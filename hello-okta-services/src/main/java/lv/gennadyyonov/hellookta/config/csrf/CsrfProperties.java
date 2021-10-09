package lv.gennadyyonov.hellookta.config.csrf;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@ConfigurationProperties(prefix = "csrf")
@ConstructorBinding
@Validated
@RequiredArgsConstructor
@Value
public class CsrfProperties {

    @NotBlank
    String cookieName;
    @NotBlank
    String headerName;
    List<String> allowedMethods;
    @Valid
    List<Endpoint> ignoredEndpoints;
    List<String> ignoredTechEndpoints;

    @Value
    static class Endpoint {

        @NotBlank
        String pattern;
        @NotBlank
        String method = GET.name();
    }
}
