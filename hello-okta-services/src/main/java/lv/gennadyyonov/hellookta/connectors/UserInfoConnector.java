package lv.gennadyyonov.hellookta.connectors;

import feign.HeaderMap;
import feign.RequestLine;
import lv.gennadyyonov.hellookta.logging.LoggingExclusion;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;

import java.net.URI;
import java.util.Map;

public interface UserInfoConnector extends ParameterLogging {

    @RequestLine("GET")
    @LoggingExclusion
    Map<String, Object> getUserInfo(URI baseUri, @LoggingExclusion @HeaderMap Map<String, Object> headers);
}
