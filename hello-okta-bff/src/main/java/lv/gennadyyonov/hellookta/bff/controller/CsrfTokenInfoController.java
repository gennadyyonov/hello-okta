package lv.gennadyyonov.hellookta.bff.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.aspects.HasRole;
import lv.gennadyyonov.hellookta.bff.dto.CsrfTokenInfo;
import lv.gennadyyonov.hellookta.config.csrf.CsrfProperties;
import lv.gennadyyonov.hellookta.logging.PerformanceLogging;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static lv.gennadyyonov.hellookta.bff.config.Constants.REST_API;
import static lv.gennadyyonov.hellookta.constants.SecurityConstants.PUBLIC_ENDPOINT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "CSRF Configuration", description = "Endpoints related to CSRF protection settings")
@PerformanceLogging(REST_API)
@RequiredArgsConstructor
@HasRole(PUBLIC_ENDPOINT)
@RestController
public class CsrfTokenInfoController {

  public static final String CSRF_TOKEN_INFO_PATH = "/config/csrfTokenInfo";

  private final CsrfProperties csrfProperties;

  @Operation(summary = "CSRF Token Info", description = "Returns CSRF Token Info")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CsrfTokenInfo.class))),
        @ApiResponse(responseCode = "4xx", description = "Bad Request", content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content)
      })
  @GetMapping(value = CSRF_TOKEN_INFO_PATH, produces = APPLICATION_JSON_VALUE)
  public CsrfTokenInfo csrfTokenInfo() {
    return CsrfTokenInfo.builder()
        .cookieName(csrfProperties.getCookieName())
        .headerName(csrfProperties.getHeaderName())
        .build();
  }
}
