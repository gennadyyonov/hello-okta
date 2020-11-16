package lv.gennadyyonov.hellookta.dto;

import lombok.Getter;

import static org.springframework.cloud.sleuth.instrument.web.TraceWebServletAutoConfiguration.TRACING_FILTER_ORDER;

@Getter
public enum FilterOrder {

    COMMONS_REQUEST_LOGGING(-10),
    USER_LOGGING(10),
    REQUEST_LOGGING(TRACING_FILTER_ORDER + 1);

    private int order;

    FilterOrder(int order) {
        this.order = order;
    }
}
