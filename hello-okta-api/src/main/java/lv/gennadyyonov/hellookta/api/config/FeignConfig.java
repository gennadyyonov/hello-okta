package lv.gennadyyonov.hellookta.api.config;

import brave.http.HttpTracing;
import brave.httpclient.TracingHttpClientBuilder;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Target;
import feign.codec.Encoder;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import lv.gennadyyonov.hellookta.connectors.UserInfoConnector;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Objects;

@Configuration
public class FeignConfig {

    private final HttpTracing httpTracing;

    @Autowired
    public FeignConfig(HttpTracing httpTracing) {
        this.httpTracing = httpTracing;
    }

    @Bean
    public UserInfoConnector userInfoConnector() {
        Client client = getClient();
        return feignBuilder(client, UserInfoConnector.class)
                .target(Target.EmptyTarget.create(UserInfoConnector.class));
    }

    private Client getClient() {
        CloseableHttpClient delegate = TracingHttpClientBuilder.create(httpTracing).build();
        return new ApacheHttpClient(delegate);
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
