package lv.gennadyyonov.hellookta.test.user;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class UserInfoListener implements TestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        UserInfo userInfo = getUserInfo(testContext);
        testContext.getApplicationContext()
            .getBeansOfType(UserInfoConfig.class)
            .values()
            .forEach(userInfoConfig -> userInfoConfig.setUp(userInfo.username(), asList(userInfo.groups())));
    }

    @Override
    public void afterTestMethod(TestContext testContext) {
        testContext.getApplicationContext()
            .getBeansOfType(UserInfoConfig.class)
            .values()
            .forEach(UserInfoConfig::reset);
    }

    private UserInfo getUserInfo(TestContext testContext) {
        UserInfo methodAnnotation = AnnotationUtils.getAnnotation(testContext.getTestMethod(), UserInfo.class);
        UserInfo classAnnotation = AnnotationUtils.getAnnotation(testContext.getTestClass(), UserInfo.class);
        return Stream.of(methodAnnotation, classAnnotation)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(AnnotationUtils.synthesizeAnnotation(UserInfo.class));
    }
}
