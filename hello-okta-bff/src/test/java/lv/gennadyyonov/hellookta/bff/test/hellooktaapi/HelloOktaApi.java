package lv.gennadyyonov.hellookta.bff.test.hellooktaapi;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;
import lv.gennadyyonov.hellookta.test.http.RequestStubbing;
import lv.gennadyyonov.hellookta.test.http.Server;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Getter
@Component
public class HelloOktaApi implements Server {

    public static final String SERVER_NAME = "helloOktaApiWireMockServer";

    private final WireMockServer delegate;

    public HelloOktaApi(@Qualifier(SERVER_NAME) WireMockServer server) {
        this.delegate = server;
    }

    public RequestStubbing onGetHello() {
        return on()
            .request(HttpMethod.GET)
            .pathEqualTo("/hello-okta-api/hello");
    }
}
