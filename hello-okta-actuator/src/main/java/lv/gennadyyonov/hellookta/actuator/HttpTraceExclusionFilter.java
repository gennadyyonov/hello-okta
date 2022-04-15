package lv.gennadyyonov.hellookta.actuator;

import org.springframework.boot.actuate.trace.http.HttpExchangeTracer;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public class HttpTraceExclusionFilter extends HttpTraceFilter {

    private final Collection<String> pathsToExclude;

    public HttpTraceExclusionFilter(HttpTraceRepository repository, HttpExchangeTracer tracer,
                                    Collection<String> pathsToExclude) {
        super(repository, tracer);
        this.pathsToExclude = pathsToExclude;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return pathsToExclude.stream().anyMatch(path -> request.getServletPath().contains(path));
    }
}
