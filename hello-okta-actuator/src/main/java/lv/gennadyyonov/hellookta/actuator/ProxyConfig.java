package lv.gennadyyonov.hellookta.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.cloud.gateway.routes.actuator", name = "enabled", havingValue = "true")
@Configuration
@PermitAll
@RestController
@RequestMapping("${spring.cloud.gateway.routes.actuator.path}")
public class ProxyConfig {

    private final ProxyProperties proxyProperties;

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS})
    public <T> ResponseEntity<T> readOperations(ProxyExchange<T> exchange, HttpServletRequest request) {
        return proxyRequest(exchange, request);
    }

    private <T> ResponseEntity<T> proxyRequest(ProxyExchange<T> exchange, HttpServletRequest request) {
        setHeaders(request, exchange);
        ProxyExchange<T> targetExchange = exchange
            .sensitive()
            .uri(getUri(exchange));
        ResponseEntity<T> entity = ofNullable(HttpMethod.resolve(request.getMethod()))
            .map(method -> execute(targetExchange, method))
            .orElseThrow(() -> new IllegalArgumentException("HTTP methd MUST NOT be null!"));
        return new ResponseEntity<>(entity.getBody(), entity.getStatusCode());
    }

    private <T> ResponseEntity<T> execute(ProxyExchange<T> exchange, HttpMethod method) {
        return switch (method) {
            case GET -> exchange.get();
            case HEAD -> exchange.head();
            case OPTIONS -> exchange.options();
            case POST -> exchange.post();
            case PUT -> exchange.put();
            case DELETE -> exchange.delete();
            default -> throw new IllegalArgumentException("HTTP method not supported : " + method + "!");
        };
    }

    private <T> void setHeaders(HttpServletRequest from, ProxyExchange<T> to) {
        Enumeration<String> headerNames = from.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            to.header(headerName, from.getHeader(headerName));
        }
    }

    private <T> String getUri(ProxyExchange<T> exchange) {
        String suffix = exchange.path(proxyProperties.getPath());
        return proxyProperties.getUrl() + suffix;
    }
}
