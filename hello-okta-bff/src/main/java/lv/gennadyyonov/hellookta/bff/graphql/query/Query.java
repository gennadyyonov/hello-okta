package lv.gennadyyonov.hellookta.bff.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {

    public String ping() {
        return "pong";
    }
}
