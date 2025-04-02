package lv.gennadyyonov.hellookta.bff.test.user;

import lv.gennadyyonov.hellookta.test.user.UserInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@UserInfo(
    username = "moon.child@gmail.com",
    groups = {"Sandbox_StandardUser"})
public @interface MoonChild {}
