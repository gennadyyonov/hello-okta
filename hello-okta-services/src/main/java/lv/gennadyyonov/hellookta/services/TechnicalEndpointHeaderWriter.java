package lv.gennadyyonov.hellookta.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.header.HeaderWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
