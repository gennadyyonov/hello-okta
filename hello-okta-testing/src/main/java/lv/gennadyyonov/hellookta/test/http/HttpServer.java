package lv.gennadyyonov.hellookta.test.http;

import com.github.tomakehurst.wiremock.WireMockServer;
import lv.gennadyyonov.hellookta.test.reset.Resettable;

public interface HttpServer extends Resettable {

    @Override
    default void reset() {
        getDelegate().resetAll();
    }

    WireMockServer getDelegate();
}
