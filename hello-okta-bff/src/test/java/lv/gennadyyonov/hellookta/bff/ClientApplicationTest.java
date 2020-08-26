package lv.gennadyyonov.hellookta.bff;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(
        properties = {
                "issuer=https://dev-220281.okta.com/oauth2/aus26efk9hrb1yASy357",
                "spring.security.oauth2.client.registration.okta.client-id=0oa26efk122CnG3k3357",
                "okta.oauth2.clientId=0oa26efk122CnG3k3357",
                "audience=api://hellookta",
                "hellooktaclient_id=0oa2m950mjcFnPPNJ357",
                "hellooktaclient_secret=IHiSqDkab2oh_pQXAscZ4BuRUb1X0yEHYl70cVgI",
                "hellooktaapiclient_baseUrl=http://localhost:8070"
        }
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientApplicationTest {

    @Test
    void contextLoads() {
    }
}
