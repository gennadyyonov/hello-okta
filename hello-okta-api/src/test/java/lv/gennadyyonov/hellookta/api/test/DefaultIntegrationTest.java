package lv.gennadyyonov.hellookta.api.test;

import lv.gennadyyonov.hellookta.api.config.WireMockInitializer;
import lv.gennadyyonov.hellookta.config.OktaResourceServerConfig;
import lv.gennadyyonov.hellookta.config.OktaServiceConfig;
import lv.gennadyyonov.hellookta.test.IntegrationTest;
import lv.gennadyyonov.hellookta.web.FilterConfig;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
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
@Import({OktaServiceConfig.class, FilterConfig.class, OktaResourceServerConfig.class})
@ContextConfiguration(initializers = WireMockInitializer.class)
public @interface DefaultIntegrationTest {
}
