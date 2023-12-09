package lv.gennadyyonov.hellookta.config.csrf;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@ConfigurationProperties(prefix = "csrf")
@Validated
@RequiredArgsConstructor
@Value
public class CsrfProperties {

    @NotNull
    Boolean csrfEnabled;
    @NotBlank
    String cookieName;
    @NotBlank
    String headerName;
    List<String> allowedMethods;
    @NotNull
    List<Endpoint> ignoredEndpoints;

    @Validated
    @Value
    static class Endpoint {

        @NotBlank
        String pattern;
        @NotBlank
        String method = GET.name();
    }
}
