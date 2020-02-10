package lv.gennadyyonov.hellookta.web;

import java.util.Map;

public interface FilterOrderProperties {

    Map<String, Integer> getFilterOrderMapping();

    default int getOrder(FilterOrder filterOrder) {
        String key = filterOrder.name();
        return getFilterOrderMapping().getOrDefault(key, filterOrder.getOrder());
    }
}
