package lv.gennadyyonov.hellookta.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CsrfTokenInfo {

    private String cookieName;
    private String headerName;
}
