package lv.gennadyyonov.hellookta.bff.graphql.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lv.gennadyyonov.hellookta.bff.graphql.type.TranslationMap;
import lv.gennadyyonov.hellookta.bff.i18n.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TranslationQuery implements GraphQLQueryResolver {

    private final TranslationService translationService;

    @Autowired
    public TranslationQuery(TranslationService translationService) {
        this.translationService = translationService;
    }

    public TranslationMap translationMap() {
        return translationService.translationMap();
    }
}
