package lv.gennadyyonov.hellookta.api.config;

import lombok.Data;
import lv.gennadyyonov.hellookta.dto.SecurityMappingProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@ConfigurationProperties(prefix = "hellooktaapi")
@Data
public class HelloOktaAPIProperties implements SecurityMappingProperties {

    private Map<String, Set<String>> securityMapping;
}