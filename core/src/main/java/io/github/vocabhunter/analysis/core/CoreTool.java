/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public final class CoreTool {
    private CoreTool() {
        // Prevent instantiation - all methods are static
    }

    @SafeVarargs
    public static <T> List<T> listOf(final T... a) {
        return Collections.unmodifiableList(Arrays.asList(a));
    }

    public static String toLowerCase(final String s) {
        return s.toLowerCase(CoreConstants.LOCALE);
    }

    public static <T> OptionalInt findLast(final List<T> list, final Predicate<T> predicate) {
        return revRange(0, list.size())
            .filter(i -> predicate.test(list.get(i)))
            .findFirst();
    }

    private static IntStream revRange(final int from, final int to) {
        return IntStream.range(from, to).map(i -> to - i + from - 1);
    }
}
