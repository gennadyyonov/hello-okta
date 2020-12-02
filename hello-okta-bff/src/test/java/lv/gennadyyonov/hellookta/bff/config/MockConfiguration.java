package lv.gennadyyonov.hellookta.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.mockito.Mockito.mock;

@Configuration
public class MockConfiguration {

    @Bean
    @Primary
    public JwtDecoder jwtDecoder() {
        return mock(JwtDecoder.class);
    }
}
