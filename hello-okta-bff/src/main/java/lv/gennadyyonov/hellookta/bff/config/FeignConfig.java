package lv.gennadyyonov.hellookta.bff.config;

import brave.http.HttpTracing;
import feign.Client;
import feign.Target;
import lv.gennadyyonov.hellookta.bff.connectors.hellooktaapi.HelloOktaApiConnector;
import lv.gennadyyonov.hellookta.config.feign.ClientCredentialsInterceptor;
import lv.gennadyyonov.hellookta.config.feign.FeignClientProvider;
import lv.gennadyyonov.hellookta.config.feign.FeignClientProviderImpl;
import lv.gennadyyonov.hellookta.config.feign.SsoInterceptor;
import lv.gennadyyonov.hellookta.services.TokenService;
import lv.gennadyyonov.hellookta.utils.FeignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class FeignConfig {

    private final HttpTracing httpTracing;
    private final SsoInterceptor ssoInterceptor;
    private final HelloOktaApiClientProperties helloOktaApiClientProperties;

    @Autowired
    public FeignConfig(HttpTracing httpTracing,
                       SsoInterceptor ssoInterceptor,
                       HelloOktaApiClientProperties helloOktaApiClientProperties) {
        this.httpTracing = httpTracing;
        this.ssoInterceptor = ssoInterceptor;
        this.helloOktaApiClientProperties = helloOktaApiClientProperties;
    }

    @Bean
    public FeignClientProvider feignClientProvider() {
        return new FeignClientProviderImpl(httpTracing);
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
    @DependsOn({"tokenService"})
    public ClientCredentialsInterceptor clientCredentialsInterceptor(TokenService tokenService) {
        return new ClientCredentialsInterceptor(tokenService, helloOktaApiClientProperties.getRunAsDetails());
    }
}
