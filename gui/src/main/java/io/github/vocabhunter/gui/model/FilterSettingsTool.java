/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.filter.FilterBuilder;
import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.analysis.session.SessionWordsTool;

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
            for (FilterFile file : settings.getFilterFiles()) {
                builder = addFilter(builder, file);
            }
        }

        return builder.build();
    }

    private static FilterBuilder addFilter(final FilterBuilder builder, final FilterFile file) {
        List<String> words = getFilteredWords(file);

        return builder.addExcludedWords(words);
    }

    private static List<String> getFilteredWords(final FilterFile file) {
        FilterFileMode mode = file.getMode();

        switch (mode) {
            case KNOWN:
                return SessionWordsTool.knownWords(file.getFile());
            case SEEN:
                return SessionWordsTool.seenWords(file.getFile());
            default:
                throw new VocabHunterException(String.format("Unknown filter mode %s", mode));
        }
    }
}
