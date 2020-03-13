package lv.gennadyyonov.hellookta.bff.i18n;

import com.fasterxml.jackson.core.type.TypeReference;
import lv.gennadyyonov.hellookta.bff.graphql.type.TranslationMap;
import lv.gennadyyonov.hellookta.bff.graphql.type.TranslationMapEntry;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static lv.gennadyyonov.hellookta.bff.utils.JsonUtils.resourceToObject;

@Service
public class TranslationService {

    private static final String RESOURCE_ROOT = "/i18n";

    public TranslationMap translationMap() {
        List<TranslationMapEntry> entries = entries(RESOURCE_ROOT + "/translation.json");
        return TranslationMap.builder()
                .entries(entries)
                .build();
    }

    @NotNull
    private List<TranslationMapEntry> entries(String name) {
        Map<String, Object> mapping = resourceToObject(name, new TypeReference<Map<String, Object>>() {
        });
        return mapping.entrySet().stream()
                .map(entry -> TranslationMapEntry.builder()
                        .key(entry.getKey())
                        .values(values(entry.getValue()))
                        .build()).collect(toList());
    }

    private List<String> values(Object value) {
        if (value instanceof String) {
            return singletonList((String) value);
        }
        if (value instanceof Collection) {
            //noinspection unchecked
            return new ArrayList<>((Collection) value);
        }
        return emptyList();
    }
}
