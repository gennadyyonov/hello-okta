package lv.gennadyyonov.hellookta.config.localhost;

import lombok.SneakyThrows;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Profiles;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import static java.util.Optional.ofNullable;
import static lv.gennadyyonov.hellookta.config.EnvironmentProfiles.LOCALHOST;

public class LocalEnvironmentEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        boolean isLocalEnvironment = ofNullable(environment)
                .map(env -> env.acceptsProfiles(Profiles.of(LOCALHOST)))
                .orElse(false);
        if (isLocalEnvironment) {
            new ProxySystemPropertiesInitializer().initialize();
            disableSSL();
        }
    }

    @SneakyThrows
    private void disableSSL() {
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
