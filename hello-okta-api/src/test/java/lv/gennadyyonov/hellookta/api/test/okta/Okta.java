package lv.gennadyyonov.hellookta.api.test.okta;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;
import lv.gennadyyonov.hellookta.test.http.Server;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Okta implements Server {

    public static final String SERVER_NAME = "oktaWireMockServer";

    private final WireMockServer delegate;

    public Okta(@Qualifier(SERVER_NAME) WireMockServer server) {
        this.delegate = server;
    }
}
