package lv.gennadyyonov.hellookta.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Value
public class User {

    @NotBlank
    String name;
    @NotBlank
    String password;
    List<String> roles;
}
