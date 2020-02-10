package lv.gennadyyonov.hellookta.config.feign;

import feign.Client;

public interface FeignClientProvider {

    Client getClient();
}
