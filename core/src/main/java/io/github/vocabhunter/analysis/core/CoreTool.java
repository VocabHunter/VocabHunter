/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public final class CoreTool {
    private static final int NANOS_PER_MILLI = 1_000_000;

    private CoreTool() {
        // Prevent instantiation - all methods are static
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

    public static String millisToNow(final long startNanos) {
        long endNanos = System.nanoTime();

        return millisBetween(startNanos, endNanos);
    }

    public static String millisBetween(final long startNanos, final long endNanos) {
        long millis = (endNanos - startNanos) / NANOS_PER_MILLI;

        return String.format("%,d", millis);
    }
}
