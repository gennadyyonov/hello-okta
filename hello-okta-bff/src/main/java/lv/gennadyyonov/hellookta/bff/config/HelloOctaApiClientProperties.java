package lv.gennadyyonov.hellookta.bff.config;

import lombok.Getter;
import lombok.Setter;
import lv.gennadyyonov.hellookta.dto.RunAsDetails;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@ConfigurationProperties("hellooktaapiclient")
public class HelloOctaApiClientProperties {

    private String baseUrl;
    private RunAsDetails runAsDetails;
}
