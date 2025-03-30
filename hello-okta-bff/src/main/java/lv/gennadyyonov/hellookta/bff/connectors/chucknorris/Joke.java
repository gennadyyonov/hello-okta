package lv.gennadyyonov.hellookta.bff.connectors.chucknorris;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.gennadyyonov.hellookta.logging.View;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Joke {

  private String id;

  @JsonView(View.LoggingView.class)
  private String value;
}
