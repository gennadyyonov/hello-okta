package lv.gennadyyonov.hellookta.bff.config;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HttpGraphQlTesterFactory {

  private final HttpGraphQlTester graphQlTester;
  private final GraphqlHttpHeadersCustomizer headersCustomizer;

  public HttpGraphQlTester createTester() {
    return graphQlTester.mutate().headers(headersCustomizer).build();
  }
}
