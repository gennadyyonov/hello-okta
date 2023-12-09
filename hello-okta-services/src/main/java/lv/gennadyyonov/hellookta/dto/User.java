package lv.gennadyyonov.hellookta.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Value
public class User {

    @NotBlank
    String name;
    @NotBlank
    String password;
    List<String> roles;
}
