package lv.gennadyyonov.hellookta.api.config;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lv.gennadyyonov.hellookta.dto.User;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@ConfigurationProperties(prefix = "user-cache")
@ConstructorBinding
@Validated
@RequiredArgsConstructor
@Value
public class UserCacheProperties {

    @Valid
    List<User> users;
}
