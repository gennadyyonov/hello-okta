package lv.gennadyyonov.hellookta.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(
        properties = {
                "issuer=https://dev-220281.okta.com/oauth2/aus26efk9hrb1yASy357",
                "spring.security.oauth2.client.registration.okta.client-id=0oa26efk122CnG3k3357",
                "okta.oauth2.clientId=0oa26efk122CnG3k3357",
                "audience=api://hellookta"
        }
)
@SpringBootTest
class ServerApplicationTests {

    @Test
    void contextLoads() {
    }
}
