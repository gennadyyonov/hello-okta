package lv.gennadyyonov.hellookta.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(
        properties = {
                "issuer=https://dev-220281.okta.com/oauth2/aus26efk9hrb1yASy357",
                "clientId=0oa26efk122CnG3k3357",
                "clientSecret=f-Th4oNxu0qzMghhtGev2vaoAPf9l7IgzHENsYny",
                "audience=api://hellookta"
        }
)
@SpringBootTest
class ServerApplicationTests {

    @Test
    void contextLoads() {
    }
}
