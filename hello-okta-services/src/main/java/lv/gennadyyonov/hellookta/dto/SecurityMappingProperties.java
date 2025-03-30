package lv.gennadyyonov.hellookta.dto;

import lv.gennadyyonov.hellookta.constants.SecurityConstants;

import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;

public interface SecurityMappingProperties {

  Map<String, Set<String>> getSecurityMapping();

  default Set<String> getAllowedUserRoles() {
    return getSecurityMappingSetValue(SecurityConstants.ALLOWED_USERS);
  }

  default Set<String> getSecurityMappingSetValue(String alias) {
    return getSecurityMapping().getOrDefault(alias, emptySet());
  }
}
