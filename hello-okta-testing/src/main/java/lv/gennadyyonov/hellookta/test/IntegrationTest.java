package lv.gennadyyonov.hellookta.test;

import lv.gennadyyonov.hellookta.test.reset.ResettingListener;
import lv.gennadyyonov.hellookta.test.user.UserInfoListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Target(TYPE)
@Retention(RUNTIME)
@Inherited
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestExecutionListeners(
    listeners = {ResettingListener.class, UserInfoListener.class},
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
public @interface IntegrationTest {
}
