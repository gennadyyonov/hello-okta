package lv.gennadyyonov.hellookta.common.dto;

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
public class Message {

  @JsonView(View.LoggingView.class)
  private String text;
}
