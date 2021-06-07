package lv.gennadyyonov.hellookta.test.user;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static lv.gennadyyonov.hellookta.test.config.TestConstants.DEFAULT_ROLE;
import static lv.gennadyyonov.hellookta.test.config.TestConstants.DEFAULT_USERNAME;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserInfo {

    @AliasFor("username")
    String value() default DEFAULT_USERNAME;

    @AliasFor("value")
    String username() default DEFAULT_USERNAME;

    String[] groups() default {DEFAULT_ROLE};
}
