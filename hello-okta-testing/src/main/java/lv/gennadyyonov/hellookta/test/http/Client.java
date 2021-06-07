package lv.gennadyyonov.hellookta.test.http;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class Client {

    private final Server.Stubbing stubbing;

    public Url request(HttpMethod method) {
        return new Url(this, method);
    }

    public Request request(HttpMethod method, UrlPattern pattern) {
        String name = ofNullable(method)
            .map(HttpMethod::name)
            .orElse(RequestMethod.ANY.getName());
        return new Request(stubbing, WireMock.request(name, pattern));
    }

    @RequiredArgsConstructor
    public static class Url {

        private final Client client;
        private final HttpMethod method;

        public Request pathEqualTo(String pattern, Object... vars) {
            return client.request(method, WireMock.urlPathEqualTo(String.format(pattern, vars)));
        }

        public Request pathMatching(String regex) {
            return client.request(method, WireMock.urlPathMatching(regex));
        }
    }
}
