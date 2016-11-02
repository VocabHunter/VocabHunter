/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import io.github.vocabhunter.analysis.core.VocabHunterException;

import java.util.function.IntPredicate;

public final class IndexTool {
    private IndexTool() {
        // Prevent instantiation - all methods are static
    }

    public static int findClosest(final int requested, final int size, final IntPredicate test) {
        int nextLower = requested;
        int nextUpper = requested;

        while (nextLower >= 0 || nextUpper < size) {
            if (nextLower >= 0 && test.test(nextLower)) {
                return nextLower;
            } else if (nextUpper < size && test.test(nextUpper)) {
                return nextUpper;
            }
            --nextLower;
            ++nextUpper;
        }

        throw new VocabHunterException("Unable to find index match");
    }
}
