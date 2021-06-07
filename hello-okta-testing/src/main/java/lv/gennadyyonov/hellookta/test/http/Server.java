package lv.gennadyyonov.hellookta.test.http;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.CountMatchingStrategy;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import lv.gennadyyonov.hellookta.test.reset.Resettable;
import org.springframework.context.SmartLifecycle;

import java.util.function.BiConsumer;

public interface Server extends Resettable, SmartLifecycle {

    default Client.Stubbing on() {
        return new Client.Stubbing((request, response) -> getDelegate().givenThat(request.willReturn(response)));
    }

    default Client.Verification verify() {
        return new Client.Verification((request, strategy) -> getDelegate().verify(strategy, request));
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

    interface Verification extends BiConsumer<RequestPatternBuilder, CountMatchingStrategy> {
    }
}
