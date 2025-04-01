package lv.gennadyyonov.hellookta.bff.graphql.query;

import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.bff.graphql.type.TranslationMap;
import lv.gennadyyonov.hellookta.bff.i18n.TranslationService;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import lv.gennadyyonov.hellookta.logging.PerformanceLogging;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import static lv.gennadyyonov.hellookta.bff.config.Constants.GRAPHQL;

@PerformanceLogging(GRAPHQL)
@Controller
@RequiredArgsConstructor
public class TranslationQuery implements ParameterLogging {

  private static final String DEFAULT_LOCALE = "en";

  private final TranslationService translationService;

  @QueryMapping
  public TranslationMap translationMap() {
    return translationService.translationMap(DEFAULT_LOCALE);
  }
}
