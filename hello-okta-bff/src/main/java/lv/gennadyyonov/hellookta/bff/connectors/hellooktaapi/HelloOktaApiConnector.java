package lv.gennadyyonov.hellookta.bff.connectors.hellooktaapi;

import feign.Headers;
import feign.RequestLine;
import lv.gennadyyonov.hellookta.common.dto.Message;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import org.springframework.http.MediaType;

import java.net.URI;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

public interface HelloOktaApiConnector extends ParameterLogging {

    @RequestLine("GET /hello")
    @Headers({CONTENT_TYPE + ": " + MediaType.TEXT_PLAIN_VALUE})
    Message hello(URI baseUri);
}
