package lv.gennadyyonov.hellookta.bff.controller;

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
public class TranslationController implements ParameterLogging {

  public static final String TRANSLATION_MAP_PATH = "/translationmap";

  private final TranslationService translationService;

  @GetMapping(value = TRANSLATION_MAP_PATH, produces = APPLICATION_JSON_VALUE)
  public TranslationMap translationMap() {
    return translationService.translationMap();
  }
}
