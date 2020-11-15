package lv.gennadyyonov.hellookta.bff.config;

import lombok.Data;
import lv.gennadyyonov.hellookta.dto.FilterOrderProperties;
import lv.gennadyyonov.hellookta.dto.SecurityMappingProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Set;

@ConfigurationProperties(prefix = "hellooktabff")
@Data
public class HelloOktaBffProps implements SecurityMappingProperties, FilterOrderProperties {

    private Map<String, Set<String>> securityMapping;
    private Map<String, Integer> filterOrderMapping;
}
