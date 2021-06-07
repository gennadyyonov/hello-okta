package lv.gennadyyonov.hellookta.test.http;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.common.Metadata;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.ContentPattern;
import com.github.tomakehurst.wiremock.matching.MultipartValuePatternBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.github.tomakehurst.wiremock.matching.ValueMatcher;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;

@RequiredArgsConstructor
public class RequestStubbing {

    private final Server.Stubbing stubbing;
    private final MappingBuilder request;

    public RequestStubbing scheme(String scheme) {
        request.withScheme(scheme);
        return this;
    }

    public RequestStubbing host(StringValuePattern hostPattern) {
        request.withHost(hostPattern);
        return this;
    }

    public RequestStubbing port(int port) {
        request.withPort(port);
        return this;
    }

    public RequestStubbing atPriority(Integer priority) {
        request.atPriority(priority);
        return this;
    }

    public RequestStubbing header(String key, StringValuePattern headerPattern) {
        request.withHeader(key, headerPattern);
        return this;
    }

    public RequestStubbing queryParam(String key, StringValuePattern queryParamPattern) {
        request.withQueryParam(key, queryParamPattern);
        return this;
    }

    public RequestStubbing queryParams(Map<String, StringValuePattern> queryParams) {
        request.withQueryParams(queryParams);
        return this;
    }

    public RequestStubbing requestBody(ContentPattern<?> bodyPattern) {
        request.withRequestBody(bodyPattern);
        return this;
    }

    public RequestStubbing multipartRequestBody(MultipartValuePatternBuilder multipartPatternBuilder) {
        request.withMultipartRequestBody(multipartPatternBuilder);
        return this;
    }

    public RequestStubbing inScenario(String scenarioName) {
        request.inScenario(scenarioName);
        return this;
    }

    public RequestStubbing id(UUID id) {
        request.withId(id);
        return this;
    }

    public RequestStubbing name(String name) {
        request.withName(name);
        return this;
    }

    public RequestStubbing persistent() {
        request.persistent();
        return this;
    }

    public RequestStubbing basicAuth(String username, String password) {
        request.withBasicAuth(username, password);
        return this;
    }

    public RequestStubbing cookie(String name, StringValuePattern cookieValuePattern) {
        request.withCookie(name, cookieValuePattern);
        return this;
    }

    public <P> RequestStubbing postServeAction(String extensionName, P parameters) {
        request.withPostServeAction(extensionName, parameters);
        return this;
    }

    public RequestStubbing metadata(Map<String, ?> metadata) {
        request.withMetadata(metadata);
        return this;
    }

    public RequestStubbing metadata(Metadata metadata) {
        request.withMetadata(metadata);
        return this;
    }

    public RequestStubbing metadata(Metadata.Builder metadata) {
        request.withMetadata(metadata);
        return this;
    }

    public RequestStubbing andMatching(ValueMatcher<Request> requestMatcher) {
        request.andMatching(requestMatcher);
        return this;
    }

    public RequestStubbing andMatching(String customRequestMatcherName) {
        request.andMatching(customRequestMatcherName);
        return this;
    }

    public RequestStubbing andMatching(String customRequestMatcherName, Parameters parameters) {
        request.andMatching(customRequestMatcherName, parameters);
        return this;
    }

    public Response expect() {
        return new Response(stubbing, request, ok());
    }
}
