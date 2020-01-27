package lv.gennadyyonov.hellookta.bff.config;

import brave.http.HttpTracing;
import brave.httpclient.TracingHttpClientBuilder;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Target;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import lv.gennadyyonov.hellookta.bff.connectors.hellooktaapi.HelloOktaApiConnector;
import lv.gennadyyonov.hellookta.connectors.TokenConnector;
import lv.gennadyyonov.hellookta.connectors.UserInfoConnector;
import lv.gennadyyonov.hellookta.services.AuthenticationService;
import lv.gennadyyonov.hellookta.services.TokenService;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import java.util.Arrays;
import java.util.Objects;

@Configuration
public class FeignConfig {

    private final HttpTracing httpTracing;
    private final SsoInterceptor ssoInterceptor;
    private final HelloOctaClientProperties helloOctaClientProperties;
    private final AuthenticationService authenticationService;

    @Autowired
    public FeignConfig(HttpTracing httpTracing,
                       SsoInterceptor ssoInterceptor,
                       HelloOctaClientProperties helloOctaClientProperties,
                       AuthenticationService authenticationService) {
        this.httpTracing = httpTracing;
        this.ssoInterceptor = ssoInterceptor;
        this.helloOctaClientProperties = helloOctaClientProperties;
        this.authenticationService = authenticationService;
    }

    @Bean
    @Qualifier("helloOktaApiConnector")
    public HelloOktaApiConnector helloOktaApiConnector() {
        Client client = getClient();
        return feignBuilder(client, HelloOktaApiConnector.class, ssoInterceptor)
                .target(Target.EmptyTarget.create(HelloOktaApiConnector.class));
    }

    @Bean
    @Qualifier("runAsHelloOktaApiConnector")
    @DependsOn({"clientCredentialsInterceptor"})
    public HelloOktaApiConnector runAsHelloOktaApiConnector(ClientCredentialsInterceptor clientCredentialsInterceptor) {
        Client client = getClient();
        return feignBuilder(client, HelloOktaApiConnector.class, clientCredentialsInterceptor)
                .target(Target.EmptyTarget.create(HelloOktaApiConnector.class));
    }

    @Bean
    @DependsOn({"tokenService"})
    public ClientCredentialsInterceptor clientCredentialsInterceptor(TokenService tokenService) {
        return new ClientCredentialsInterceptor(tokenService);
    }

    @Bean
    public UserInfoConnector userInfoConnector() {
        Client client = getClient();
        return feignBuilder(client, UserInfoConnector.class)
                .target(Target.EmptyTarget.create(UserInfoConnector.class));
    }

    @Bean
    public TokenConnector tokenConnector() {
        Client client = getClient();
        return feignBuilder(client, TokenConnector.class, feignFormEncoder())
                .target(Target.EmptyTarget.create(TokenConnector.class));
    }

    @Bean
    public TokenService tokenService(TokenConnector tokenConnector) {
        ClientCredentialsResourceDetails clientCredentialsResourceDetails = helloOctaClientProperties.getClientCredentialsResourceDetails();
        return new TokenService(clientCredentialsResourceDetails, tokenConnector, authenticationService);
    }

    private Client getClient() {
        CloseableHttpClient delegate = TracingHttpClientBuilder.create(httpTracing).build();
        return new ApacheHttpClient(delegate);
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
