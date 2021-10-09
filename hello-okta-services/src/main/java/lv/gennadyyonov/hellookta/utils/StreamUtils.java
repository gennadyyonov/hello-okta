package lv.gennadyyonov.hellookta.utils;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@UtilityClass
public class StreamUtils {

    public static <T> Stream<T> getNullableStream(T obj) {
        return Optional.ofNullable(obj)
            .map(Stream::of)
            .orElseGet(Stream::empty);
    }

    public static <T> Stream<T> getNullableFlatStream(Collection<T> elements) {
        return getNullableStream(elements).flatMap(Collection::stream);
    }
}
