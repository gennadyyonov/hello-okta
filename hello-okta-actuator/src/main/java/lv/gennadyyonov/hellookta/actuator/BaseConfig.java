package lv.gennadyyonov.hellookta.actuator;

import org.springframework.boot.actuate.endpoint.web.PathMappedEndpoints;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfig {

    @Bean
    public PathService pathService(PathMappedEndpoints pathMappedEndpoints) {
        return new PathService(pathMappedEndpoints);
    }
}
