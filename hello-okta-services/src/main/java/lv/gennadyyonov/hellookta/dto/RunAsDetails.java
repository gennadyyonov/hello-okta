package lv.gennadyyonov.hellookta.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.gennadyyonov.hellookta.logging.View;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RunAsDetails {

    @JsonView(View.LoggingView.class)
    private String grantType;
    private String clientId;
    private String clientSecret;
    @JsonView(View.LoggingView.class)
    private String accessTokenUri;
    @JsonView(View.LoggingView.class)
    private List<String> scope;
}
