/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CollectionTool {
    private CollectionTool() {
        // Prevent instantiation - all methods are static
    }

    @SafeVarargs
    public static <T> List<T> listOf(final T... a) {
        return Collections.unmodifiableList(Arrays.asList(a));
    }
}
