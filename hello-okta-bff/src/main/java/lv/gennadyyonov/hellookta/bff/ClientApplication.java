package lv.gennadyyonov.hellookta.bff;

import lv.gennadyyonov.hellookta.bff.config.HelloOctaClientProperties;
import lv.gennadyyonov.hellookta.bff.config.HelloOktaBFFProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({HelloOctaClientProperties.class, HelloOktaBFFProperties.class})
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
