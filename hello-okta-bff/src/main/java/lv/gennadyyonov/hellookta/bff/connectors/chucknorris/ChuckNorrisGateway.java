package lv.gennadyyonov.hellookta.bff.connectors.chucknorris;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.config.HelloOktaBffProps;
import org.springframework.stereotype.Service;

import java.net.URI;

@RequiredArgsConstructor
@Service
public class ChuckNorrisGateway {

    private final HelloOktaBffProps helloOktaBffProps;
    private final ChuckNorrisConnector chuckNorrisConnector;

    @SneakyThrows
    public Joke randomJoke() {
        URI baseUri = new URI(helloOktaBffProps.getChuckNorrisUrl());
        return chuckNorrisConnector.randomJoke(baseUri);
    }
}
