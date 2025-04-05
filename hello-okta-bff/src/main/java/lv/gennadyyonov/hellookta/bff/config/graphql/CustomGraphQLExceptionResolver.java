package lv.gennadyyonov.hellookta.bff.config.graphql;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import lv.gennadyyonov.hellookta.bff.config.ErrorCodes;
import lv.gennadyyonov.hellookta.exception.AccessDeniedException;
import lv.gennadyyonov.hellookta.exception.ExternalSystemException;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CustomGraphQLExceptionResolver implements DataFetcherExceptionResolver {

  @Override
  public Mono<List<GraphQLError>> resolveException(
      Throwable exception, DataFetchingEnvironment environment) {
    if (exception instanceof AccessDeniedException) {
      return handleException(exception, environment, ErrorType.FORBIDDEN, ErrorCodes.ACCESS_DENIED);
    }
    if (exception instanceof ExternalSystemException) {
      return handleException(
          exception, environment, ErrorType.INTERNAL_ERROR, ErrorCodes.EXTERNAL_SYSTEM);
    }
    return Mono.empty();
  }

  private Mono<List<GraphQLError>> handleException(
      Throwable exception,
      DataFetchingEnvironment environment,
      ErrorType errorType,
      String errorCode) {
    log.error("Handling {} exception: {}", errorType, exception.getMessage());
    GraphQLError error =
        GraphqlErrorBuilder.newError()
            .errorType(errorType)
            .message(exception.getMessage())
            .path(environment.getExecutionStepInfo().getPath())
            .location(environment.getField().getSourceLocation())
            .extensions(Map.of("code", errorCode))
            .build();
    return Mono.just(List.of(error));
  }
}
