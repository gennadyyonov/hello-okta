package lv.gennadyyonov.hellookta.bff.config.graphql;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import lv.gennadyyonov.hellookta.exception.AccessDeniedException;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CustomGraphQLExceptionResolver implements DataFetcherExceptionResolver {

  private static final String ACCESS_DENIED_ERROR_ID = "SC.ER.ACCESSDENIED";

  @Override
  public Mono<List<GraphQLError>> resolveException(
      Throwable exception, DataFetchingEnvironment environment) {
    if (exception instanceof AccessDeniedException) {
      log.error("Handling access denied exception: {}", exception.getMessage());
      GraphQLError error =
          GraphqlErrorBuilder.newError()
              .errorType(ErrorType.FORBIDDEN)
              .message(exception.getMessage())
              .path(environment.getExecutionStepInfo().getPath())
              .location(environment.getField().getSourceLocation())
              .extensions(Map.of("code", ACCESS_DENIED_ERROR_ID))
              .build();
      return Mono.just(List.of(error));
    }
    return Mono.empty();
  }
}
