package lv.gennadyyonov.hellookta.bff.graphql.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {

    public String ping() {
        return "pong";
    }
}
