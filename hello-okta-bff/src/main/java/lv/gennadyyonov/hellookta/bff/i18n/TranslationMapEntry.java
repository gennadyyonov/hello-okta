package lv.gennadyyonov.hellookta.bff.i18n;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Represents a key-value pair in the translation map.")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TranslationMapEntry {

  @Schema(description = "The unique key for the translated text.", example = "greeting")
  private String key;

  @Schema(description = "The translated text corresponding to the key.", example = "Hello")
  private String value;
}
