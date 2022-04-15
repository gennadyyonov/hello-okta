package lv.gennadyyonov.hellookta.actuator;

import org.springframework.boot.actuate.trace.http.HttpExchangeTracer;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

import static java.util.Collections.singletonList;

@Configuration
public class HttpTracingConfig {

    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

    @Bean
    public HttpTraceFilter httpTraceFilter(PathService pathService,
                                           HttpTraceRepository repository, HttpExchangeTracer tracer) {
        Collection<String> basePaths = singletonList(pathService.getBasePath());
        return new HttpTraceExclusionFilter(repository, tracer, basePaths);
    }
}
