package lv.gennadyyonov.hellookta.bff.test.chucknorris;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;
import lombok.experimental.Delegate;
import lv.gennadyyonov.hellookta.test.http.HttpServer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ChuckNorris implements HttpServer {

    public static final String SERVER_NAME = "chuckNorrisWireMockServer";

    @Delegate
    private final WireMockServer delegate;

    public ChuckNorris(@Qualifier(SERVER_NAME) WireMockServer server) {
        this.delegate = server;
    }
}
