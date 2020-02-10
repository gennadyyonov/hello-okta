package lv.gennadyyonov.hellookta.config.feign;

import brave.http.HttpTracing;
import brave.httpclient.TracingHttpClientBuilder;
import feign.Client;
import feign.httpclient.ApacheHttpClient;
import lombok.SneakyThrows;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

public class LocalhostFeignClientProvider implements FeignClientProvider {

    private final HttpTracing httpTracing;

    public LocalhostFeignClientProvider(HttpTracing httpTracing) {
        this.httpTracing = httpTracing;
    }

    @Override
    public Client getClient() {
        return new ApacheHttpClient(createHttpClient());
    }

    @SneakyThrows
    private CloseableHttpClient createHttpClient() {
        TrustStrategy trustAllStrategy = (chain, authType) -> true;
        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadTrustMaterial(null, trustAllStrategy)
                .build();
        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);
        return TracingHttpClientBuilder.create(httpTracing)
                .setSSLSocketFactory(connectionFactory)
                .useSystemProperties()
                .build();
    }

}
