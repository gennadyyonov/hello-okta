package lv.gennadyyonov.hellookta.bff.i18n;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Represents a collection of translations for a specific locale.")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TranslationMap {

  @Schema(description = "The locale for the translations.", example = "en")
  private String locale;

  @ArraySchema(arraySchema = @Schema(description = "A list of translation key-value pairs."))
  @Builder.Default
  private List<TranslationMapEntry> entries = new ArrayList<>();
}
