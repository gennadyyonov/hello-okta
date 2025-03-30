package lv.gennadyyonov.hellookta.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;

@Slf4j
@UtilityClass
public class OktaUtils {

  private static final String OKTA = "okta";

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

  public static String getClientId(OAuth2ClientProperties properties) {
    return ofNullable(properties)
        .map(OAuth2ClientProperties::getRegistration)
        .map(map -> map.get(OKTA))
        .map(OAuth2ClientProperties.Registration::getClientId)
        .orElse(null);
  }

  public static String getIssuerUri(OAuth2ClientProperties properties) {
    return ofNullable(properties)
        .map(OAuth2ClientProperties::getProvider)
        .map(map -> map.get(OKTA))
        .map(OAuth2ClientProperties.Provider::getIssuerUri)
        .orElse(null);
  }
}
