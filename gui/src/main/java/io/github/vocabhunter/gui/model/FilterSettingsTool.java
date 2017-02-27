/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.filter.FilterBuilder;
import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.analysis.grid.FilterFileWordsExtractor;
import io.github.vocabhunter.analysis.settings.BaseListedFile;

import java.util.List;
import javax.inject.Inject;

public class FilterSettingsTool {
    @Inject
    private FilterFileWordsExtractor extractor;

    public WordFilter filter(final FilterSettings settings, final boolean isFilterEnabled) {
        FilterBuilder builder = new FilterBuilder();

        if (isFilterEnabled) {
            builder = builder.minimumLetters(settings.getMinimumLetters())
                .minimumOccurrences(settings.getMinimumOccurrences());

            if (!settings.isAllowInitialCapitals()) {
                builder = builder.excludeInitialCapital();
            }
            for (BaseListedFile file : settings.getFilterFiles()) {
                builder = addFilter(builder, file);
            }
        }

        return builder.build();
    }

    private FilterBuilder addFilter(final FilterBuilder builder, final BaseListedFile file) {
        List<String> words = extractor.extract(file);

        return builder.addExcludedWords(words);
    }
}
