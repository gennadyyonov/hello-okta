package lv.gennadyyonov.hellookta.api.config.info;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import static java.util.Collections.singletonMap;

@Component
public class OktaApiInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("okta-api-info", singletonMap("name", "Hello Okta API"));
    }
}
