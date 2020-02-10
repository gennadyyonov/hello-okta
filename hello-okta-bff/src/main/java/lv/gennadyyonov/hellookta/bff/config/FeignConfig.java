package lv.gennadyyonov.hellookta.bff.config;

import brave.http.HttpTracing;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Target;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import lv.gennadyyonov.hellookta.bff.connectors.hellooktaapi.HelloOktaApiConnector;
import lv.gennadyyonov.hellookta.configuration.feign.ClientCredentialsInterceptor;
import lv.gennadyyonov.hellookta.configuration.feign.FeignClientProvider;
import lv.gennadyyonov.hellookta.configuration.feign.FeignClientProviderImpl;
import lv.gennadyyonov.hellookta.configuration.feign.SsoInterceptor;
import lv.gennadyyonov.hellookta.connectors.TokenConnector;
import lv.gennadyyonov.hellookta.connectors.UserInfoConnector;
import lv.gennadyyonov.hellookta.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Arrays;
import java.util.Objects;

@Configuration
public class FeignConfig {

    private final HttpTracing httpTracing;
    private final SsoInterceptor ssoInterceptor;
    private final HelloOctaApiClientProperties helloOctaApiClientProperties;

    @Autowired
    public FeignConfig(HttpTracing httpTracing,
                       SsoInterceptor ssoInterceptor,
                       HelloOctaApiClientProperties helloOctaApiClientProperties) {
        this.httpTracing = httpTracing;
        this.ssoInterceptor = ssoInterceptor;
        this.helloOctaApiClientProperties = helloOctaApiClientProperties;
    }

    @Bean
    public FeignClientProvider feignClientProvider() {
        return new FeignClientProviderImpl(httpTracing);
    }

    @Bean
    @Qualifier("helloOktaApiConnector")
    public HelloOktaApiConnector helloOktaApiConnector(FeignClientProvider feignClientProvider) {
        Client client = feignClientProvider.getClient();
        return feignBuilder(client, HelloOktaApiConnector.class, ssoInterceptor)
                .target(Target.EmptyTarget.create(HelloOktaApiConnector.class));
    }

    @Bean
    @Qualifier("runAsHelloOktaApiConnector")
    @DependsOn({"clientCredentialsInterceptor"})
    public HelloOktaApiConnector runAsHelloOktaApiConnector(FeignClientProvider feignClientProvider,
                                                            ClientCredentialsInterceptor clientCredentialsInterceptor) {
        Client client = feignClientProvider.getClient();
        return feignBuilder(client, HelloOktaApiConnector.class, clientCredentialsInterceptor)
                .target(Target.EmptyTarget.create(HelloOktaApiConnector.class));
    }

    @Bean
    @DependsOn({"tokenService"})
    public ClientCredentialsInterceptor clientCredentialsInterceptor(TokenService tokenService) {
        return new ClientCredentialsInterceptor(tokenService, helloOctaApiClientProperties.getRunAsDetails());
    }

    @Bean
    public UserInfoConnector userInfoConnector(FeignClientProvider feignClientProvider) {
        Client client = feignClientProvider.getClient();
        return feignBuilder(client, UserInfoConnector.class)
                .target(Target.EmptyTarget.create(UserInfoConnector.class));
    }

    @Bean
    public TokenConnector tokenConnector(FeignClientProvider feignClientProvider) {
        Client client = feignClientProvider.getClient();
        return feignBuilder(client, TokenConnector.class, feignFormEncoder())
                .target(Target.EmptyTarget.create(TokenConnector.class));
    }

    private Encoder feignFormEncoder() {
        return new FormEncoder(new JacksonEncoder());
    }

    private Feign.Builder feignBuilder(Client client,
                                       Class clazz,
                                       RequestInterceptor... requestInterceptors) {
        return feignBuilder(client, clazz, new JacksonEncoder(), requestInterceptors);
    }

    private Feign.Builder feignBuilder(Client client,
                                       Class clazz,
                                       Encoder encoder,
                                       RequestInterceptor... requestInterceptors) {
        Feign.Builder builder = Feign.builder()
                .client(client)
                .encoder(encoder)
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(clazz))
                .logLevel(Logger.Level.FULL);
        Arrays.stream(requestInterceptors).filter(Objects::nonNull).forEach(builder::requestInterceptor);
        return builder;
    }
}
