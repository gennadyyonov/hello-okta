package lv.gennadyyonov.hellookta.bff.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import lv.gennadyyonov.hellookta.logging.PerformanceLogging;
import org.springframework.stereotype.Component;

import static lv.gennadyyonov.hellookta.bff.config.Constants.GRAPHQL;

@PerformanceLogging(GRAPHQL)
@Component
public class Query implements GraphQLQueryResolver, ParameterLogging {

    public String ping() {
        return "pong";
    }
}
