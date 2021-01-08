package lv.gennadyyonov.hellookta.api.config;

import lv.gennadyyonov.hellookta.config.OktaResourceServerConfig;
import lv.gennadyyonov.hellookta.config.OktaServiceConfig;
import lv.gennadyyonov.hellookta.web.FilterConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

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
@Import({OktaServiceConfig.class, FilterConfig.class, OktaResourceServerConfig.class})
@ContextConfiguration(initializers = {WireMockInitializer.class})
public @interface IntegrationTest {
}
