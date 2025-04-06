package lv.gennadyyonov.hellookta.test.setup;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class InitializationListener implements TestExecutionListener {

  @Override
  public void beforeTestMethod(TestContext testContext) {
    testContext
        .getApplicationContext()
        .getBeansOfType(Initializable.class)
        .values()
        .forEach(Initializable::setUp);
  }

  @Override
  public void afterTestMethod(TestContext context) {
    context
        .getApplicationContext()
        .getBeansOfType(Initializable.class)
        .values()
        .forEach(Initializable::reset);
  }
}
