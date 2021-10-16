package lv.gennadyyonov.hellookta.bff.config.cors;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ConfigurationProperties(prefix = "cors")
@RequiredArgsConstructor
@ConstructorBinding
@Validated
@Value
public class CorsProperties {

    @NotBlank
    String urlPattern;
    Boolean allowCredentials;
    List<String> allowedOrigins;
    @NotNull
    List<String> allowedHeaders;
    @NotNull
    List<String> allowedMethods;
}
