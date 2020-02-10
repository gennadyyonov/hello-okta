package lv.gennadyyonov.hellookta.utils;

import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Objects;

@UtilityClass
public class FeignUtils {

    public static Encoder feignFormEncoder() {
        return new FormEncoder(new JacksonEncoder());
    }

    public static Feign.Builder feignBuilder(Client client,
                                             Class clazz,
                                             RequestInterceptor... requestInterceptors) {
        return FeignUtils.feignBuilder(client, clazz, new JacksonEncoder(), requestInterceptors);
    }

    public static Feign.Builder feignBuilder(Client client,
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
