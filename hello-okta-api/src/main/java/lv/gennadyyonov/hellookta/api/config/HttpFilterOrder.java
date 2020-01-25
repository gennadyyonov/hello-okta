package lv.gennadyyonov.hellookta.api.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpFilterOrder {

    public static final int COMMONS_REQUEST_LOGGING_ORDER = -10;
    public static final int USER_LOGGING_ORDER = 10;
    public static final int REQUEST_LOGGING_ORDER = 20;
}
