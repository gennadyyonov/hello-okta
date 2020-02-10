package lv.gennadyyonov.hellookta.config.feign;

import brave.http.HttpTracing;
import brave.httpclient.TracingHttpClientBuilder;
import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.impl.client.CloseableHttpClient;

public class FeignClientProviderImpl implements FeignClientProvider {

    private final HttpTracing httpTracing;

    public FeignClientProviderImpl(HttpTracing httpTracing) {
        this.httpTracing = httpTracing;
    }

    @Override
    public Client getClient() {
        CloseableHttpClient delegate = TracingHttpClientBuilder.create(httpTracing)
                .useSystemProperties()
                .build();
        return new ApacheHttpClient(delegate);
    }
}
