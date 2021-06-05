package lv.gennadyyonov.hellookta.bff.test.okta;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;
import lombok.experimental.Delegate;
import lv.gennadyyonov.hellookta.test.http.HttpServer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Okta implements HttpServer {

    public static final String SERVER_NAME = "oktaWireMockServer";

    @Delegate
    private final WireMockServer delegate;

    public Okta(@Qualifier(SERVER_NAME) WireMockServer server) {
        this.delegate = server;
    }
}
