package lv.gennadyyonov.hellookta.bff.connectors.hellooktaapi;

import feign.Contract;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lv.gennadyyonov.hellookta.bff.config.HelloOktaApiClientProperties;
import lv.gennadyyonov.hellookta.config.feign.FeignInterceptorProvider;
import org.springframework.context.annotation.Bean;

public class RunAsHelloOktaApiConnectorConfig {

    @Bean
    public Contract contract() {
        return new Contract.Default();
    }

    @Bean
    public Logger.Level loggerLevel() {
        return Logger.Level.HEADERS;
    }

    @Bean
    public Encoder encoder() {
        return new JacksonEncoder();
    }

    @Bean
    public Decoder decoder() {
        return new JacksonDecoder();
    }

    @Bean
    public RequestInterceptor clientCredentialsInterceptor(FeignInterceptorProvider feignInterceptorProvider,
                                                           HelloOktaApiClientProperties helloOktaApiClientProperties) {
        return feignInterceptorProvider.getClientCredentialsInterceptor(helloOktaApiClientProperties.getRunAsDetails());
    }
}
