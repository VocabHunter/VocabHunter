/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import io.github.vocabhunter.analysis.model.AnalysisWord;

import java.util.List;
import java.util.stream.Collectors;

public final class FilterTool {
    private FilterTool() {
        // Prevent instantiation - all methods are static
    }

    public static boolean isValid(final WordFilter filter, final List<? extends AnalysisWord> words) {
        return words.stream()
            .anyMatch(filter::isShown);
    }

    public static <T extends AnalysisWord> List<T> applyFilter(final WordFilter filter, final List<T> words) {
        return words.stream()
                .filter(filter::isShown)
                .collect(Collectors.toList());
    }
}
