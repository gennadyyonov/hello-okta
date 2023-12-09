package lv.gennadyyonov.hellookta.bff.connectors.chucknorris;

import feign.Headers;
import feign.RequestLine;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@FeignClient(name = "chuckNorrisConnector", configuration = ChuckNorrisConnectorConfig.class)
public interface ChuckNorrisConnector extends ParameterLogging {

    @RequestLine("GET /jokes/random")
    @Headers({CONTENT_TYPE + ": " + MediaType.APPLICATION_JSON_VALUE})
    Joke randomJoke();
}
