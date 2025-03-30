package lv.gennadyyonov.hellookta.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@UtilityClass
public class RequestUtils {

  public static HttpServletRequest getHttpServletRequest() {
    return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
        .filter(requestAttributes -> requestAttributes instanceof ServletRequestAttributes)
        .map(requestAttributes -> ((ServletRequestAttributes) requestAttributes).getRequest())
        .orElse(null);
  }
}
