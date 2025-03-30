package lv.gennadyyonov.hellookta.dto;

import java.util.Map;

public interface FilterOrderProperties {

  Map<String, Integer> getFilterOrderMapping();

  default int getOrder(FilterOrder filterOrder) {
    String key = filterOrder.name();
    return getFilterOrderMapping().getOrDefault(key, filterOrder.getOrder());
  }
}
