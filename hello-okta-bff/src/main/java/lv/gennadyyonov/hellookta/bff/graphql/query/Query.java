package lv.gennadyyonov.hellookta.bff.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.aspects.HasRole;
import lv.gennadyyonov.hellookta.bff.connectors.chucknorris.ChuckNorrisGateway;
import lv.gennadyyonov.hellookta.bff.connectors.chucknorris.Joke;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import lv.gennadyyonov.hellookta.logging.PerformanceLogging;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;
import static lv.gennadyyonov.hellookta.bff.config.Constants.GRAPHQL;
import static lv.gennadyyonov.hellookta.constants.SecurityConstants.ALLOWED_USERS;

@RequiredArgsConstructor
@PerformanceLogging(GRAPHQL)
@HasRole(ALLOWED_USERS)
@Component
public class Query implements GraphQLQueryResolver, ParameterLogging {

    private static final String PONG = "pong";

    private final ChuckNorrisGateway chuckNorrisGateway;

    public String ping() {
        return ofNullable(chuckNorrisGateway.randomJoke())
                .map(Joke::getValue)
                .orElse(PONG);
    }
}
