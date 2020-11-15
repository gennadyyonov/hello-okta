package lv.gennadyyonov.hellookta.api.client.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@UtilityClass
public class LocalhostUtils {

    @SneakyThrows
    public static void disableSsl() {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = {new TrustAllManager()};
        // Install the all-trusting trust manager
        sslContext.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = (hostname, session) -> true;
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    private static class TrustAllManager implements X509TrustManager {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
            // NOP
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
            // NOP
        }
    }
}
