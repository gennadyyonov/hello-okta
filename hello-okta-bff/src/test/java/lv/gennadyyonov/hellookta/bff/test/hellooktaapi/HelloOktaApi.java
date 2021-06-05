package lv.gennadyyonov.hellookta.bff.test.hellooktaapi;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;
import lombok.experimental.Delegate;
import lv.gennadyyonov.hellookta.test.http.HttpServer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Getter
@Component
public class HelloOktaApi implements HttpServer {

    public static final String SERVER_NAME = "helloOktaApiWireMockServer";

    @Delegate
    private final WireMockServer delegate;

    public HelloOktaApi(@Qualifier(SERVER_NAME) WireMockServer server) {
        this.delegate = server;
    }
}
