package lv.gennadyyonov.hellookta.bff.controller;

import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.aspects.HasRole;
import lv.gennadyyonov.hellookta.bff.dto.CsrfTokenInfo;
import lv.gennadyyonov.hellookta.config.csrf.CsrfProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static lv.gennadyyonov.hellookta.constants.SecurityConstants.ALLOWED_USERS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@HasRole(ALLOWED_USERS)
@RestController
public class CsrfTokenInfoController {

    public static final String CSRF_TOKEN_INFO_SUFFIX = "/config/csrfTokenInfo";

    private final CsrfProperties csrfProperties;

    @GetMapping(value = CSRF_TOKEN_INFO_SUFFIX, produces = APPLICATION_JSON_VALUE)
    public CsrfTokenInfo csrfTokenInfo() {
        return CsrfTokenInfo.builder()
            .cookieName(csrfProperties.getCookieName())
            .headerName(csrfProperties.getHeaderName())
            .build();
    }
}
