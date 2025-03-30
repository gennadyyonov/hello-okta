package lv.gennadyyonov.hellookta.bff.graphql.query;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import lombok.SneakyThrows;
import lv.gennadyyonov.hellookta.bff.test.DefaultIntegrationTest;
import lv.gennadyyonov.hellookta.test.user.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultIntegrationTest
class UserInfoQueryTest {

  @Autowired private GraphQLTestTemplate graphQLTestTemplate;

  @UserInfo("jane.smith@gmail.com")
  @SneakyThrows
  @Test
  void me() {
    GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/me.graphql");

    assertThat(response.isOk()).isTrue();
    assertThat(response.get("$.data.me.userId")).isEqualTo("JANE.SMITH@GMAIL.COM");
    assertThat(response.get("$.data.me.firstName")).isEqualTo("Jane");
    assertThat(response.get("$.data.me.lastName")).isEqualTo("Smith");
    assertThat(response.get("$.data.me.email")).isEqualTo("Jane.Smith@gmail.com");
    List<String> roles = response.getList("$.data.me.roles", String.class);
    assertThat(roles).hasSize(4);
    assertThat(roles)
        .contains("HelloOkta_StandardUser", "SCOPE_openid", "SCOPE_email", "SCOPE_profile");
  }
}
