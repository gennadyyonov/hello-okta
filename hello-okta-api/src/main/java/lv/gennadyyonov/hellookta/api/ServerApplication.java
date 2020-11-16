package lv.gennadyyonov.hellookta.api;

import lv.gennadyyonov.hellookta.api.config.HelloOktaApiProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableConfigurationProperties({HelloOktaApiProps.class})
@SpringBootApplication
public class ServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
