package lv.gennadyyonov.hellookta.test.reset;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class ResettingListener implements TestExecutionListener {

  @Override
  public void beforeTestMethod(TestContext testContext) {
    testContext
        .getApplicationContext()
        .getBeansOfType(Resettable.class)
        .values()
        .forEach(Resettable::reset);
  }
}
