package lv.gennadyyonov.hellookta.test.http;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import lv.gennadyyonov.hellookta.test.reset.Resettable;
import org.springframework.context.SmartLifecycle;

import java.util.function.BiConsumer;

public interface Server extends Resettable, SmartLifecycle {

    default Client on() {
        return new Client((request, response) -> getDelegate().givenThat(request.willReturn(response)));
    }

    @Override
    default void start() {
        getDelegate().start();
    }

    @Override
    default void stop() {
        getDelegate().stop();
    }

    @Override
    default boolean isRunning() {
        return getDelegate().isRunning();
    }

    @Override
    default void reset() {
        getDelegate().resetAll();
    }

    WireMockServer getDelegate();

    interface Stubbing extends BiConsumer<MappingBuilder, ResponseDefinitionBuilder> {
    }
}
