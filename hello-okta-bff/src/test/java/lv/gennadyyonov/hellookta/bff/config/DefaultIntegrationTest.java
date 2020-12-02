package lv.gennadyyonov.hellookta.bff.config;

import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Inherited
@TestPropertySource("/testpropertysource/default.properties")
@IntegrationTest
public @interface DefaultIntegrationTest {
}
