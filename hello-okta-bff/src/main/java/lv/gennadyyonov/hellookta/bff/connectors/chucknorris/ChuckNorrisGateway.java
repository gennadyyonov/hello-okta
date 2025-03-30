package lv.gennadyyonov.hellookta.bff.connectors.chucknorris;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChuckNorrisGateway {

  private final ChuckNorrisConnector chuckNorrisConnector;

  @SneakyThrows
  public Joke randomJoke() {
    return chuckNorrisConnector.randomJoke();
  }
}
