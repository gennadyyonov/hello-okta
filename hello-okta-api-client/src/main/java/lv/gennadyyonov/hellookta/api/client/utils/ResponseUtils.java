package lv.gennadyyonov.hellookta.api.client.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ResponseUtils {

    public static String jsonAttributeValueRegex(String attribute) {
        return ".*\"" + attribute + "\"\\s*:\\s*\"([^\"]+)\".*";
    }

    public static String extractValueByPattern(Pattern pattern, String response) {
        Matcher matcher = pattern.matcher(response);
        if (matcher.matches() && matcher.groupCount() > 0) {
            return matcher.group(1);
        }
        return null;
    }
}
