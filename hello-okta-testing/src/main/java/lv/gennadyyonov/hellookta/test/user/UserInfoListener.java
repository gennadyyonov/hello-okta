package lv.gennadyyonov.hellookta.test.user;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class UserInfoListener implements TestExecutionListener {

  @Override
  public void beforeTestMethod(TestContext context) {
    getUserInfo(context)
        .ifPresent(
            userInfo ->
                context
                    .getApplicationContext()
                    .getBeansOfType(UserInfoConfig.class)
                    .values()
                    .forEach(
                        config -> config.setUp(userInfo.username(), asList(userInfo.groups()))));
  }

  @Override
  public void afterTestMethod(TestContext context) {
    context
        .getApplicationContext()
        .getBeansOfType(UserInfoConfig.class)
        .values()
        .forEach(UserInfoConfig::reset);
  }

  private Optional<UserInfo> getUserInfo(TestContext context) {
    UserInfo methodAnnotation =
        AnnotationUtils.getAnnotation(context.getTestMethod(), UserInfo.class);
    UserInfo classAnnotation =
        AnnotationUtils.getAnnotation(context.getTestClass(), UserInfo.class);
    return Stream.of(methodAnnotation, classAnnotation).filter(Objects::nonNull).findFirst();
  }
}
