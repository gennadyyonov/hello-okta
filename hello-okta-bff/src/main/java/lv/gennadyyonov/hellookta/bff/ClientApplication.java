package lv.gennadyyonov.hellookta.bff;

import lv.gennadyyonov.hellookta.bff.config.HelloOktaApiClientProperties;
import lv.gennadyyonov.hellookta.bff.config.HelloOktaBffProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(
    basePackages = {
      "lv.gennadyyonov.hellookta.connectors",
      "lv.gennadyyonov.hellookta.bff.connectors"
    })
@EnableConfigurationProperties({HelloOktaApiClientProperties.class, HelloOktaBffProps.class})
@SpringBootApplication
public class ClientApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}
