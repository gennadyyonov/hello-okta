package lv.gennadyyonov.hellookta.aspects;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasRole {

    @AliasFor("alias")
    String value() default "";

    @AliasFor("value")
    String alias() default "";

    String[] roles() default {};
}
