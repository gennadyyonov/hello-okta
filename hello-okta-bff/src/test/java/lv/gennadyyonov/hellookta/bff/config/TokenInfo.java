package lv.gennadyyonov.hellookta.bff.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TokenInfo {

  private String username;
  private List<String> groups;
}
