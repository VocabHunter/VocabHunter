/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.filter.FilterBuilder;
import io.github.vocabhunter.analysis.filter.FilterTool;
import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.analysis.model.AnalysisWord;

import java.util.List;

public final class FilterSettingsTool {
    private FilterSettingsTool() {
        // Prevent instantiation - all methods are static
    }

    public static WordFilter filter(final FilterSettings settings, final boolean isFilterEnabled) {
        FilterBuilder builder = new FilterBuilder();

        if (isFilterEnabled) {
            builder = builder.minimumLetters(settings.getMinimumLetters())
                .minimumOccurrences(settings.getMinimumOccurrences());

            if (!settings.isAllowInitialCapitals()) {
                builder = builder.excludeInitialCapital();
            }

            return builder.build();
        } else {
            return builder.build();
        }
    }

    public static boolean isValid(final FilterSettings settings, final List<? extends AnalysisWord> words) {
        return FilterTool.isValid(filter(settings, true), words);
    }
}
