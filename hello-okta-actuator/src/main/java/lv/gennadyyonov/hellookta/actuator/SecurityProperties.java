package lv.gennadyyonov.hellookta.actuator;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties(prefix = "management.endpoints.security")
@Validated
@RequiredArgsConstructor
@Value
public class SecurityProperties {

    Boolean enabled;
    List<String> allowedRoles;
}
