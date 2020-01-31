package lv.gennadyyonov.hellookta.configuration.feign;

import feign.Client;

public interface FeignClientProvider {

    Client getClient();
}
