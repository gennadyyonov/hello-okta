package lv.gennadyyonov.hellookta.bff.config;

import feign.Client;
import feign.Target;
import lv.gennadyyonov.hellookta.bff.connectors.chucknorris.ChuckNorrisConnector;
import lv.gennadyyonov.hellookta.bff.connectors.hellooktaapi.HelloOktaApiConnector;
import lv.gennadyyonov.hellookta.config.feign.ClientCredentialsInterceptor;
import lv.gennadyyonov.hellookta.config.feign.FeignClientProvider;
import lv.gennadyyonov.hellookta.config.feign.SsoInterceptor;
import lv.gennadyyonov.hellookta.services.TokenService;
import lv.gennadyyonov.hellookta.utils.FeignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BffFeignConfig {

    private final SsoInterceptor ssoInterceptor;
    private final HelloOktaApiClientProperties helloOktaApiClientProperties;

    @Autowired
    public BffFeignConfig(SsoInterceptor ssoInterceptor,
                          HelloOktaApiClientProperties helloOktaApiClientProperties) {
        this.ssoInterceptor = ssoInterceptor;
        this.helloOktaApiClientProperties = helloOktaApiClientProperties;
    }

    @Bean
    @Qualifier("helloOktaApiConnector")
    public HelloOktaApiConnector helloOktaApiConnector(FeignClientProvider feignClientProvider) {
        Client client = feignClientProvider.getClient();
        return FeignUtils.feignBuilder(client, HelloOktaApiConnector.class, ssoInterceptor)
                .target(Target.EmptyTarget.create(HelloOktaApiConnector.class));
    }

    @Bean
    @Qualifier("runAsHelloOktaApiConnector")
    @DependsOn({"clientCredentialsInterceptor"})
    public HelloOktaApiConnector runAsHelloOktaApiConnector(FeignClientProvider feignClientProvider,
                                                            ClientCredentialsInterceptor clientCredentialsInterceptor) {
        Client client = feignClientProvider.getClient();
        return FeignUtils.feignBuilder(client, HelloOktaApiConnector.class, clientCredentialsInterceptor)
                .target(Target.EmptyTarget.create(HelloOktaApiConnector.class));
    }

    @Bean
    public ChuckNorrisConnector chuckNorrisConnector(FeignClientProvider feignClientProvider) {
        Client client = feignClientProvider.getClient();
        return FeignUtils.feignBuilder(client, ChuckNorrisConnector.class)
                .target(Target.EmptyTarget.create(ChuckNorrisConnector.class));
    }

    @Bean
    @DependsOn({"tokenService"})
    public ClientCredentialsInterceptor clientCredentialsInterceptor(TokenService tokenService) {
        return new ClientCredentialsInterceptor(tokenService, helloOktaApiClientProperties.getRunAsDetails());
    }
}
