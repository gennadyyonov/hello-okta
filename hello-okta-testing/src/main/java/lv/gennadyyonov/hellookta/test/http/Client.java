package lv.gennadyyonov.hellookta.test.http;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

import static java.util.Optional.ofNullable;

public interface Client<T> {

    default Url<T> request(HttpMethod httpMethod) {
        return new Url<>(this, httpMethod);
    }

    T request(HttpMethod httpMethod, UrlPattern pattern);

    @RequiredArgsConstructor
    class Url<T> {

        private final Client<T> client;
        private final HttpMethod httpMethod;

        public T pathEqualTo(String pattern, Object... vars) {
            return client.request(httpMethod, WireMock.urlPathEqualTo(String.format(pattern, vars)));
        }

        public T pathMatching(String regex) {
            return client.request(httpMethod, WireMock.urlPathMatching(regex));
        }
    }

    @RequiredArgsConstructor
    class Stubbing implements Client<RequestStubbing> {

        private final Server.Stubbing stubbing;

        @Override
        public RequestStubbing request(HttpMethod httpMethod, UrlPattern pattern) {
            String name = ofNullable(httpMethod)
                .map(HttpMethod::name)
                .orElse(RequestMethod.ANY.getName());
            return new RequestStubbing(stubbing, WireMock.request(name, pattern));
        }
    }

    @RequiredArgsConstructor
    class Verification implements Client<RequestVerification> {

        private final Server.Verification verification;

        @Override
        public RequestVerification request(HttpMethod httpMethod, UrlPattern pattern) {
            RequestMethod requestMethod = ofNullable(httpMethod)
                .map(method -> RequestMethod.fromString(httpMethod.name()))
                .orElse(RequestMethod.ANY);
            RequestPatternBuilder request = new RequestPatternBuilder(requestMethod, pattern);
            return new RequestVerification(verification, request);
        }
    }
}
