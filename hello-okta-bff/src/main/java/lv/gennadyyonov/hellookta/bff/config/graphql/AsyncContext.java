package lv.gennadyyonov.hellookta.bff.config.graphql;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;

@Data
@Builder
public class AsyncContext {

  private Map<String, String> mdcContextMap;
  private RequestAttributes requestAttributes;
  private SecurityContext securityContext;
}
