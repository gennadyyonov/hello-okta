package lv.gennadyyonov.hellookta.test.http;

import com.github.tomakehurst.wiremock.client.BasicCredentials;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.ContentPattern;
import com.github.tomakehurst.wiremock.matching.MultipartValuePattern;
import com.github.tomakehurst.wiremock.matching.MultipartValuePatternBuilder;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.github.tomakehurst.wiremock.matching.ValueMatcher;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RequestVerification {

    private final Server.Verification verification;
    private final RequestPatternBuilder request;

    public RequestVerification but() {
        request.but();
        return this;
    }

    public RequestVerification scheme(String scheme) {
        request.withScheme(scheme);
        return this;
    }

    public RequestVerification host(StringValuePattern hostPattern) {
        request.withHost(hostPattern);
        return this;
    }

    public RequestVerification port(int port) {
        request.withPort(port);
        return this;
    }

    public RequestVerification url(String url) {
        request.withUrl(url);
        return this;
    }

    public RequestVerification header(String key, StringValuePattern valuePattern) {
        request.withHeader(key, valuePattern);
        return this;
    }

    public RequestVerification noHeader(String key) {
        request.withoutHeader(key);
        return this;
    }

    public RequestVerification queryParam(String key, StringValuePattern valuePattern) {
        request.withQueryParam(key, valuePattern);
        return this;
    }

    public RequestVerification cookie(String key, StringValuePattern valuePattern) {
        request.withCookie(key, valuePattern);
        return this;
    }

    public RequestVerification basicAuth(BasicCredentials basicCredentials) {
        request.withBasicAuth(basicCredentials);
        return this;
    }

    public RequestVerification requestBody(ContentPattern valuePattern) {
        request.withRequestBody(valuePattern);
        return this;
    }

    public RequestVerification requestBodyPart(MultipartValuePattern multiPattern) {
        request.withRequestBodyPart(multiPattern);
        return this;
    }

    public RequestVerification anyRequestBodyPart(MultipartValuePatternBuilder multiPatternBuilder) {
        request.withAnyRequestBodyPart(multiPatternBuilder);
        return this;
    }

    public RequestVerification allRequestBodyParts(MultipartValuePatternBuilder multiPatternBuilder) {
        request.withAllRequestBodyParts(multiPatternBuilder);
        return this;
    }

    public RequestVerification andMatching(ValueMatcher<Request> customMatcher) {
        request.andMatching(customMatcher);
        return this;
    }

    public RequestVerification andMatching(String customRequestMatcherName) {
        request.andMatching(customRequestMatcherName);
        return this;
    }

    public RequestVerification andMatching(String customRequestMatcherName, Parameters parameters) {
        request.andMatching(customRequestMatcherName, parameters);
        return this;
    }

    public RequestCount wasCalled() {
        return new RequestCount(verification, request);
    }
}
