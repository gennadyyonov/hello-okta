package lv.gennadyyonov.hellookta.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.header.HeaderWriter;

@RequiredArgsConstructor
public class TechnicalEndpointHeaderWriter implements HeaderWriter {

  private final TechnicalEndpointService technicalEndpointService;
  private final HeaderWriter delegate;

  @Override
  public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
    if (!technicalEndpointService.isWhitelistedUrl(request)) {
      delegate.writeHeaders(request, response);
    }
  }
}
