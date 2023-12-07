package lv.gennadyyonov.hellookta.bff.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lv.gennadyyonov.hellookta.dto.RunAsDetails;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@ConfigurationProperties("hello-okta-api")
@RequiredArgsConstructor
@Validated
@Getter
public class HelloOktaApiClientProperties {

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
