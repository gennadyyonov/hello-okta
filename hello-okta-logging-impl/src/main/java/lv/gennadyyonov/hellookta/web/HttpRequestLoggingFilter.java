package lv.gennadyyonov.hellookta.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
public class HttpRequestLoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // NOOP
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest request) {
            long start = System.currentTimeMillis();
            String url = getRequestUrl(request);
            log.info("Performing HTTP {} request to {}", request.getMethod(), url);
            try {
                chain.doFilter(servletRequest, servletResponse);
            } finally {
                long millis = System.currentTimeMillis() - start;
                log.info(
                        "Execution time of HTTP request to {} = {} ms. Status = {}.",
                        url, millis, ((HttpServletResponse) servletResponse).getStatus()
                );
            }
        }
    }

    private String getRequestUrl(HttpServletRequest request) {
        StringBuilder buf = new StringBuilder(request.getRequestURL());
        String queryString = request.getQueryString();
        if (isNotEmpty(queryString)) {
            buf.append("?").append(queryString);
        }
        return buf.toString();
    }

    @Override
    public void destroy() {
        // NOOP
    }
}

