package lv.gennadyyonov.hellookta.bff.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.gennadyyonov.hellookta.logging.View;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @JsonView(View.LoggingView.class)
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    @JsonView(View.LoggingView.class)
    private Set<String> roles;
}
