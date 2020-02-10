package lv.gennadyyonov.hellookta.web;

import lombok.Getter;

@Getter
public enum FilterOrder {

    COMMONS_REQUEST_LOGGING(-10),
    USER_LOGGING(10),
    REQUEST_LOGGING(20);

    private int order;

    FilterOrder(int order) {
        this.order = order;
    }
}
