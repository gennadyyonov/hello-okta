package lv.gennadyyonov.hellookta.bff.graphql.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TranslationMap {

    private String locale;
    @Builder.Default
    private List<TranslationMapEntry> entries = new ArrayList<>();
}
