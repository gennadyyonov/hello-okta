package lv.gennadyyonov.hellookta.bff.graphql.query;

import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.aspects.HasRole;
import lv.gennadyyonov.hellookta.bff.connectors.hellooktaapi.HelloOktaApiGateway;
import lv.gennadyyonov.hellookta.bff.graphql.type.AuthType;
import lv.gennadyyonov.hellookta.common.dto.Message;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import lv.gennadyyonov.hellookta.logging.PerformanceLogging;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import static lv.gennadyyonov.hellookta.bff.config.Constants.GRAPHQL;
import static lv.gennadyyonov.hellookta.bff.graphql.type.AuthType.USER;
import static lv.gennadyyonov.hellookta.constants.SecurityConstants.ALLOWED_USERS;

@PerformanceLogging(GRAPHQL)
@HasRole(ALLOWED_USERS)
@RequiredArgsConstructor
@Controller
public class HelloQuery implements ParameterLogging {

  private final HelloOktaApiGateway helloOktaApiGateway;

  @QueryMapping
  public Message hello(@Argument AuthType authType) {
    return (authType == USER) ? helloOktaApiGateway.helloUser() : helloOktaApiGateway.helloClient();
  }
}
