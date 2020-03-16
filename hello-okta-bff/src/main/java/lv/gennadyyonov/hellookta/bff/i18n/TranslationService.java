package lv.gennadyyonov.hellookta.bff.i18n;

import com.fasterxml.jackson.core.type.TypeReference;
import lv.gennadyyonov.hellookta.bff.graphql.type.TranslationMap;
import lv.gennadyyonov.hellookta.bff.graphql.type.TranslationMapEntry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static lv.gennadyyonov.hellookta.bff.utils.JsonUtils.resourceToObject;

@Service
public class TranslationService {

    private static final String RESOURCE_ROOT = "/i18n";

    public TranslationMap translationMap(String locale) {
        List<TranslationMapEntry> entries = entries(resourceName(locale));
        return TranslationMap.builder()
                .locale(locale)
                .entries(entries)
                .build();
    }

    private String resourceName(String locale) {
        return RESOURCE_ROOT + format("/%s-translation.json", locale);
    }

    private List<TranslationMapEntry> entries(String name) {
        //noinspection unchecked
        Map<String, String> mapping = resourceToObject(name, new TypeReference<Map>() {
        });
        return mapping.entrySet().stream()
                .map(entry -> TranslationMapEntry.builder()
                        .key(entry.getKey())
                        .value(entry.getValue())
                        .build()).collect(toList());
    }
}
