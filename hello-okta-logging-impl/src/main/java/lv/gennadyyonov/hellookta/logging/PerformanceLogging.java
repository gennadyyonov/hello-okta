package lv.gennadyyonov.hellookta.logging;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static lv.gennadyyonov.hellookta.logging.LoggingUtils.DEFAULT;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface PerformanceLogging {

    @AliasFor("layer")
    String value() default DEFAULT;

    @AliasFor("value")
    String layer() default DEFAULT;
}
