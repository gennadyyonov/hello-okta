package lv.gennadyyonov.hellookta.bff.config.graphql;

import graphql.GraphQLError;
import graphql.kickstart.spring.error.ThrowableGraphQLError;
import lombok.extern.slf4j.Slf4j;
import lv.gennadyyonov.hellookta.exception.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Component
public class CustomGraphQLErrorConfig {

  private static final String ACCESS_DENIED_ERROR_ID = "SC.ER.ACCESSDENIED";

  @ExceptionHandler(AccessDeniedException.class)
  public GraphQLError handle(AccessDeniedException e) {
    log.error("Handling access denied exception : {}", e.getMessage());
    return new ThrowableGraphQLError(e, ACCESS_DENIED_ERROR_ID);
  }
}
