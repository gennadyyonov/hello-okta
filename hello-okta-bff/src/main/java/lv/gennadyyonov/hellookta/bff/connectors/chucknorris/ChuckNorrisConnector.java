package lv.gennadyyonov.hellookta.bff.connectors.chucknorris;

import feign.Headers;
import feign.RequestLine;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import org.springframework.http.MediaType;

import java.net.URI;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

public interface ChuckNorrisConnector extends ParameterLogging {

    @RequestLine("GET /jokes/random")
    @Headers({CONTENT_TYPE + ": " + MediaType.APPLICATION_JSON_VALUE})
    Joke randomJoke(URI baseUri);
}
