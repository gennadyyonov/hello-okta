package lv.gennadyyonov.hellookta.api.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

import static lv.gennadyyonov.hellookta.api.client.utils.LocalhostUtils.disableSSL;

@Slf4j
public class HelloClientDemo {

    private static final String PROPERTIES_FILE = "/demo.properties";

    public static void main(String[] args) {
        disableSSL();
        Properties properties = loadProperties();
        String issuer = properties.getProperty("issuer");
        String clientId = properties.getProperty("clientId");
        String clientSecret = properties.getProperty("clientSecret");
        String scope = properties.getProperty("scope");
        String serverUri = properties.getProperty("serverUri");
        String text = greeting(issuer, clientId, clientSecret, scope, serverUri);
        log.info("Text : {}", text);
    }

    @SneakyThrows
    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream in = HelloClientDemo.class.getResourceAsStream(PROPERTIES_FILE)) {
            properties.load(in);
            return properties;
        }
    }

    @SneakyThrows
    private static String greeting(String issuer, String clientId, String clientSecret,
                                   String scope, String serverUri) {
        ClientConfig config = ClientConfig.builder()
                .issuer(issuer)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .scope(scope)
                .serverUri(serverUri)
                .build();
        HelloClient<String> client = new DefaultHelloClient(config);
        return client.hello();
    }
}