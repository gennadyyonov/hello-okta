package lv.gennadyyonov.hellookta.bff.config.graphql;

import lombok.experimental.UtilityClass;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static org.slf4j.MDC.getCopyOfContextMap;

@UtilityClass
public class AsyncContextUtils {

    static AsyncContext createAsyncContext() {
        Map<String, String> mdcContextMap = ofNullable(getCopyOfContextMap()).orElseGet(HashMap::new);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return AsyncContext.builder()
                .mdcContextMap(mdcContextMap)
                .requestAttributes(requestAttributes)
                .securityContext(securityContext)
                .build();
    }

    static void populate(AsyncContext asyncContext) {
        asyncContext.getMdcContextMap().forEach(MDC::put);
        RequestContextHolder.setRequestAttributes(asyncContext.getRequestAttributes());
    }
}
