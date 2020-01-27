package lv.gennadyyonov.hellookta.bff.graphql.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lv.gennadyyonov.hellookta.aspects.HasRole;
import lv.gennadyyonov.hellookta.bff.connectors.hellooktaapi.HelloOktaApiGateway;
import lv.gennadyyonov.hellookta.bff.graphql.type.AuthType;
import lv.gennadyyonov.hellookta.common.dto.Message;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static lv.gennadyyonov.hellookta.bff.graphql.type.AuthType.USER;
import static lv.gennadyyonov.hellookta.constants.SecurityConstants.ALLOWED_USERS;

@HasRole(ALLOWED_USERS)
@Component
public class HelloQuery implements GraphQLQueryResolver, ParameterLogging {

    private final HelloOktaApiGateway helloOktaApiGateway;

    @Autowired
    public HelloQuery(HelloOktaApiGateway helloOktaApiGateway) {
        this.helloOktaApiGateway = helloOktaApiGateway;
    }

    public Message hello(AuthType authType) {
        return (authType == USER) ? helloOktaApiGateway.helloUser() : helloOktaApiGateway.helloClient();
    }
}
