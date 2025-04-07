package lv.gennadyyonov.hellookta.bff.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.aspects.HasRole;
import lv.gennadyyonov.hellookta.bff.i18n.TranslationMap;
import lv.gennadyyonov.hellookta.bff.i18n.TranslationService;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import lv.gennadyyonov.hellookta.logging.PerformanceLogging;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static lv.gennadyyonov.hellookta.bff.config.Constants.REST_API;
import static lv.gennadyyonov.hellookta.constants.SecurityConstants.PUBLIC_ENDPOINT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@PerformanceLogging(REST_API)
@HasRole(PUBLIC_ENDPOINT)
@RestController
@RequiredArgsConstructor
@Tag(name = "Translation", description = "Endpoints for retrieving translation data")
public class TranslationController implements ParameterLogging {

  public static final String TRANSLATION_MAP_PATH = "/translationmap";

  private final TranslationService translationService;

  @Operation(
      summary = "Retrieve Translation Map",
      description = "Returns a map of translations for a specific locale.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved translation map.",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TranslationMap.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request - Invalid parameters",
            content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content)
      })
  @GetMapping(value = TRANSLATION_MAP_PATH, produces = APPLICATION_JSON_VALUE)
  public TranslationMap translationMap() {
    return translationService.translationMap();
  }
}
