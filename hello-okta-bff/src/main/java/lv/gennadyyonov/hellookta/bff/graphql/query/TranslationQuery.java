package lv.gennadyyonov.hellookta.bff.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lv.gennadyyonov.hellookta.bff.graphql.type.TranslationMap;
import lv.gennadyyonov.hellookta.bff.i18n.TranslationService;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import lv.gennadyyonov.hellookta.logging.PerformanceLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static lv.gennadyyonov.hellookta.bff.config.Constants.GRAPHQL;

@PerformanceLogging(GRAPHQL)
@Component
public class TranslationQuery implements GraphQLQueryResolver, ParameterLogging {

  private static final String DEFAULT_LOCALE = "en";

  private final TranslationService translationService;

  @Autowired
  public TranslationQuery(TranslationService translationService) {
    this.translationService = translationService;
  }

  public TranslationMap translationMap() {
    return translationService.translationMap(DEFAULT_LOCALE);
  }
}
