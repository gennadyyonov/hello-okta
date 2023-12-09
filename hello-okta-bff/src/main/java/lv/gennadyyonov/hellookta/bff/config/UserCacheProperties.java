package lv.gennadyyonov.hellookta.bff.config;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lv.gennadyyonov.hellookta.dto.User;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties(prefix = "user-cache")
@Validated
@RequiredArgsConstructor
@Value
public class UserCacheProperties {

    List<User> users;
}
