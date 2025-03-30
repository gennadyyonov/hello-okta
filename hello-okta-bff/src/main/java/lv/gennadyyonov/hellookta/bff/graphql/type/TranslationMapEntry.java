package lv.gennadyyonov.hellookta.bff.graphql.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TranslationMapEntry {

  private String key;
  private String value;
}
