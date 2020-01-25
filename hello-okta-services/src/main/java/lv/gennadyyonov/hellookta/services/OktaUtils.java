package lv.gennadyyonov.hellookta.services;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import static java.util.Collections.emptySet;

@Slf4j
@UtilityClass
public class OktaUtils {

    public static Collection<String> extractCollection(Map<String, Object> claims, String key) {
        if (!CollectionUtils.isEmpty(claims) && StringUtils.hasText(key)) {
            Object value = claims.get(key);
            if (value instanceof Collection) {
                //noinspection unchecked
                return new HashSet<>((Collection<String>) value);
            } else if (value != null) {
                log.warn("Could not extract collection from claim '{}', value was not a collection", key);
            }
        }
        return emptySet();
    }
}
