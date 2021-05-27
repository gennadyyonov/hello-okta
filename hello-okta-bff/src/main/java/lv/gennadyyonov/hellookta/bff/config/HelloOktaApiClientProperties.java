package lv.gennadyyonov.hellookta.bff.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lv.gennadyyonov.hellookta.dto.RunAsDetails;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ConfigurationProperties("hello-okta-api")
@RequiredArgsConstructor
@ConstructorBinding
@Validated
@Getter
public class HelloOktaApiClientProperties {

    @NotNull
    @Valid
    private final String baseUrl;
    @NotNull
    @Valid
    private final Client client;

    @RequiredArgsConstructor
    @Getter
    @Value
    public static class Client {

        @NotNull
        @Valid
        RunAsDetails runAsDetails;
    }
}
