package lv.gennadyyonov.hellookta.config.localhost;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URL;

@RequiredArgsConstructor
public class ProxySystemPropertiesInitializer {

    private static final String HTTPS_PROXY = "HTTPS_PROXY";
    private static final String HTTP_PROXY = "HTTP_PROXY";
    private static final String NO_PROXY = "NO_PROXY";
    private static final String HTTPS_PROXY_HOST = "https.proxyHost";
    private static final String HTTPS_PROXY_PORT = "https.proxyPort";
    private static final String HTTP_PROXY_HOST = "http.proxyHost";
    private static final String HTTP_PROXY_PORT = "http.proxyPort";
    private static final String HTTP_NO_PROXY_HOSTS = "http.noProxyHosts";

    public void initialize() {
        moveFromSystemEnvToProperty(HTTPS_PROXY, HTTPS_PROXY_HOST, HTTPS_PROXY_PORT);
        moveFromSystemEnvToProperty(HTTP_PROXY, HTTP_PROXY_HOST, HTTP_PROXY_PORT);
        String noProxy = System.getenv(NO_PROXY);
        if (StringUtils.isNoneBlank(noProxy)) {
            System.setProperty(HTTP_NO_PROXY_HOSTS, noProxy);
        }
    }

    @SneakyThrows
    private void moveFromSystemEnvToProperty(String proxyEnvVariableName,
                                             String hostPropertyName,
                                             String portPropertyName) {
        var proxy = System.getenv(proxyEnvVariableName);
        if (StringUtils.isNoneBlank(proxy)) {
            URL url = new URI(proxy).toURL();
            System.setProperty(hostPropertyName, url.getHost());
            System.setProperty(portPropertyName, Integer.toString(url.getPort()));
        }
    }
}
