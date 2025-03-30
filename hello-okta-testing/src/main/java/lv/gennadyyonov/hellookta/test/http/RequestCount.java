package lv.gennadyyonov.hellookta.test.http;

import com.github.tomakehurst.wiremock.client.CountMatchingStrategy;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import lombok.RequiredArgsConstructor;

import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.lessThanOrExactly;
import static com.github.tomakehurst.wiremock.client.WireMock.moreThanOrExactly;

@RequiredArgsConstructor
public class RequestCount {

  private final Server.Verification verification;
  private final RequestPatternBuilder request;

  public void never() {
    times(0);
  }

  public void once() {
    times(1);
  }

  public void times(int count) {
    endVerification(exactly(count));
  }

  public void atLeast(int count) {
    endVerification(moreThanOrExactly(count));
  }

  public void atMost(int count) {
    endVerification(lessThanOrExactly(count));
  }

  private void endVerification(CountMatchingStrategy strategy) {
    verification.accept(request, strategy);
  }
}
