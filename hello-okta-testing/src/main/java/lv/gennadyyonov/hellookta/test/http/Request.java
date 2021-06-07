package lv.gennadyyonov.hellookta.test.http;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.matching.ContentPattern;
import com.github.tomakehurst.wiremock.matching.MultipartValuePatternBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;

@RequiredArgsConstructor
public class Request {

    private final Server.Stubbing stubbing;
    private final MappingBuilder request;

    public Request scheme(String scheme) {
        request.withScheme(scheme);
        return this;
    }

    public Request host(StringValuePattern hostPattern) {
        request.withHost(hostPattern);
        return this;
    }

    public Request port(int port) {
        request.withPort(port);
        return this;
    }

    public Request header(String key, StringValuePattern headerPattern) {
        request.withHeader(key, headerPattern);
        return this;
    }

    public Request queryParam(String key, StringValuePattern queryParamPattern) {
        request.withQueryParam(key, queryParamPattern);
        return this;
    }

    public Request queryParams(Map<String, StringValuePattern> queryParams) {
        request.withQueryParams(queryParams);
        return this;
    }

    public Request requestBody(ContentPattern<?> bodyPattern) {
        request.withRequestBody(bodyPattern);
        return this;
    }

    public Request multipartRequestBody(MultipartValuePatternBuilder multipartPatternBuilder) {
        request.withMultipartRequestBody(multipartPatternBuilder);
        return this;
    }

    public Request basicAuth(String username, String password) {
        request.withBasicAuth(username, password);
        return this;
    }

    public Request cookie(String name, StringValuePattern cookieValuePattern) {
        request.withCookie(name, cookieValuePattern);
        return this;
    }

    public Response expect() {
        return new Response(stubbing, request, ok());
    }
}
