package lv.gennadyyonov.hellookta.dto;

import lombok.Getter;
import org.springframework.core.Ordered;

@Getter
public enum FilterOrder {

    COMMONS_REQUEST_LOGGING(-10),
    USER_LOGGING(10),
    REQUEST_LOGGING(Ordered.HIGHEST_PRECEDENCE + 10);

    private int order;

    FilterOrder(int order) {
        this.order = order;
    }
}
