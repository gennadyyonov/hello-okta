package lv.gennadyyonov.hellookta.bff.connectors.chucknorris;

import feign.Contract;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;

public class ChuckNorrisConnectorConfig {

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
}
