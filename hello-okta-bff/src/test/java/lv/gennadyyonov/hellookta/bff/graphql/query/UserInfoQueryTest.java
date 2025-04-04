package lv.gennadyyonov.hellookta.bff.graphql.query;

import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.config.HttpGraphQlTesterFactory;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.bff.test.user.MoonChild;
import lv.gennadyyonov.hellookta.test.user.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.execution.ErrorType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultIntegrationTest
class UserInfoQueryTest {

  @Autowired private HttpGraphQlTesterFactory graphQlTesterFactory;

  @UserInfo("jane.smith@gmail.com")
  @SneakyThrows
  @Test
  void me() {
    var graphQlTester = graphQlTesterFactory.createTester();
    var response =
        graphQlTester
            .documentName("me")
            .execute()
            .path("data.me")
            .entity(lv.gennadyyonov.hellookta.dto.UserInfo.class)
            .get();

    assertThat(response.getUserId()).isEqualTo("JANE.SMITH@GMAIL.COM");
    assertThat(response.getFirstName()).isEqualTo("Jane");
    assertThat(response.getLastName()).isEqualTo("Smith");
    assertThat(response.getEmail()).isEqualTo("Jane.Smith@gmail.com");
    var roles = response.getRoles();
    assertThat(roles)
        .containsExactlyInAnyOrder(
            "HelloOkta_StandardUser", "SCOPE_openid", "SCOPE_email", "SCOPE_profile");
  }

  @MoonChild
  @SneakyThrows
  @Test
  void forbidden() {
    var graphQlTester = graphQlTesterFactory.createTester();
    graphQlTester
        .documentName("me")
        .execute()
        .errors()
        .satisfy(
            errors -> {
              assertThat(errors).hasSize(1);
              var error = errors.getFirst();
              assertThat(error.getErrorType()).isEqualTo(ErrorType.FORBIDDEN);
              assertThat(error.getMessage())
                  .isEqualTo("User 'MOON.CHILD@GMAIL.COM' has no access to UserInfoQuery.me()");
              assertThat(error.getPath()).isEqualTo("me");
              assertThat(error.getExtensions())
                  .containsAllEntriesOf(
                      Map.of("code", "HO.ER.ACCESSDENIED", "classification", "FORBIDDEN"));
            });
  }
}
