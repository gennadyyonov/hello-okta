package lv.gennadyyonov.hellookta.bff.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lv.gennadyyonov.hellookta.bff.graphql.type.TranslationMap;
import lv.gennadyyonov.hellookta.bff.i18n.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TranslationQuery implements GraphQLQueryResolver {

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
