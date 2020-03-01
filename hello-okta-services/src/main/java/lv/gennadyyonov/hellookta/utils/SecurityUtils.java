package lv.gennadyyonov.hellookta.utils;

import lombok.experimental.UtilityClass;
import lv.gennadyyonov.hellookta.dto.UserInfo;

import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static lv.gennadyyonov.hellookta.constants.SecurityConstants.PUBLIC_ENDPOINT;

@UtilityClass
public class SecurityUtils {

    public boolean hasAnyRoles(UserInfo profile, String[] roles) {
        return stream(roles).anyMatch(role -> profile.getRoles().contains(role));
    }

    public boolean hasAnyRoles(UserInfo profile, Stream<String> rolesStream) {
        return rolesStream.anyMatch(role -> profile.getRoles().contains(role));
    }

    public boolean shouldBypassSecurityValidation(String alias) {
        return PUBLIC_ENDPOINT.equals(alias);
    }
}