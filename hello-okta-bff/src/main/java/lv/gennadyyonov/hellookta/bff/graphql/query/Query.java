package lv.gennadyyonov.hellookta.bff.graphql.query;

import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.aspects.HasRole;
import lv.gennadyyonov.hellookta.bff.connectors.chucknorris.ChuckNorrisGateway;
import lv.gennadyyonov.hellookta.bff.connectors.chucknorris.Joke;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import lv.gennadyyonov.hellookta.logging.PerformanceLogging;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import static java.util.Optional.ofNullable;
import static lv.gennadyyonov.hellookta.bff.config.Constants.GRAPHQL;
import static lv.gennadyyonov.hellookta.constants.SecurityConstants.ALLOWED_USERS;

@RequiredArgsConstructor
@PerformanceLogging(GRAPHQL)
@HasRole(ALLOWED_USERS)
@Controller
public class Query implements ParameterLogging {

  private static final String PONG = "pong";

  private final ChuckNorrisGateway chuckNorrisGateway;

  @QueryMapping
  public String ping() {
    return ofNullable(chuckNorrisGateway.randomJoke()).map(Joke::getValue).orElse(PONG);
  }
}
