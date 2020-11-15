package lv.gennadyyonov.hellookta.bff;

import lv.gennadyyonov.hellookta.bff.config.HelloOctaApiClientProperties;
import lv.gennadyyonov.hellookta.bff.config.HelloOktaBffProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@EnableConfigurationProperties({HelloOctaApiClientProperties.class, HelloOktaBffProps.class})
@SpringBootApplication
public class ClientApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
