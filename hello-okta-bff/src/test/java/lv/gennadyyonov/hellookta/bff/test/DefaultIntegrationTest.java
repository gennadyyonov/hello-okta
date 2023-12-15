package lv.gennadyyonov.hellookta.bff.test;

import lv.gennadyyonov.hellookta.bff.config.WireMockInitializer;
import lv.gennadyyonov.hellookta.config.okta.OktaResourceServerConfig;
import lv.gennadyyonov.hellookta.config.okta.OktaServiceConfig;
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
