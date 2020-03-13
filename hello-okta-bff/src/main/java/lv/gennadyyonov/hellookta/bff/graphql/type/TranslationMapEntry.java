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
public class TranslationMapEntry {

    private String key;
    @Builder.Default
    private List<String> values = new ArrayList<>();
}
