package lv.gennadyyonov.hellookta.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

  @Bean
  public ParameterLoggingAspect parameterLoggingAspect() {
    return new ParameterLoggingAspect();
  }

  @Bean
  public PerformanceLoggingAspect performanceLoggingAspect() {
    return new PerformanceLoggingAspect();
  }
}
