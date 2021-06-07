package lv.gennadyyonov.hellookta.bff.test.chucknorris;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;
import lv.gennadyyonov.hellookta.test.http.RequestStubbing;
import lv.gennadyyonov.hellookta.test.http.Server;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ChuckNorris implements Server {

    public static final String SERVER_NAME = "chuckNorrisWireMockServer";

    private final WireMockServer delegate;

    public ChuckNorris(@Qualifier(SERVER_NAME) WireMockServer server) {
        this.delegate = server;
    }

    public RequestStubbing onGetRandomJoke() {
        return on()
            .request(HttpMethod.GET)
            .pathEqualTo("/chuck-norris/jokes/random");
    }
}
