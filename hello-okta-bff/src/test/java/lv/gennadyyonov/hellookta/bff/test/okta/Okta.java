package lv.gennadyyonov.hellookta.bff.test.okta;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;
import lv.gennadyyonov.hellookta.test.http.RequestStubbing;
import lv.gennadyyonov.hellookta.test.http.Server;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;

@Getter
@Component
public class Okta implements Server {

  public static final String SERVER_NAME = "oktaWireMockServer";

  private final WireMockServer delegate;

  public Okta(@Qualifier(SERVER_NAME) WireMockServer server) {
    this.delegate = server;
  }

  public RequestStubbing onGetUserInfo() {
    return on().request(HttpMethod.GET).pathEqualTo("/okta/oauth2/default/v1/userinfo");
  }

  public RequestStubbing onPostToken(String grantType, String scope) {
    return on().request(HttpMethod.POST)
        .pathEqualTo("/okta/oauth2/default/v1/token")
        .requestBody(containing("grant_type=" + grantType))
        .requestBody(containing("scope=" + scope));
  }
}
