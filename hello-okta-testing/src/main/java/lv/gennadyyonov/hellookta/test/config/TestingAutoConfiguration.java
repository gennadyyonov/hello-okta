package lv.gennadyyonov.hellookta.test.config;

import lv.gennadyyonov.hellookta.test.user.AuthUserInfoConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(AuthUserInfoConfig.class)
@Configuration(proxyBeanMethods = false)
public class TestingAutoConfiguration {}
