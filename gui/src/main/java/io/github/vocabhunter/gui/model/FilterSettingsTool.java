/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.core.DelayedExecutor;
import io.github.vocabhunter.analysis.core.ThreadPoolTool;
import io.github.vocabhunter.analysis.filter.FilterBuilder;
import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.analysis.grid.FilterFileWordsExtractor;
import io.github.vocabhunter.analysis.settings.BaseListedFile;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FilterSettingsTool {
    private final FilterFileWordsExtractor extractor;

    private final DelayedExecutor executor;

    @Inject
    public FilterSettingsTool(final FilterFileWordsExtractor extractor, final ThreadPoolTool threadPoolTool) {
        this.extractor = extractor;
        this.executor = threadPoolTool.filterThreadPool();
    }

    public WordFilter filter(final FilterSettings settings) {
        FilterBuilder builder = new FilterBuilder();

        builder.executor(executor);
        builder = builder.minimumLetters(settings.getMinimumLetters())
            .minimumOccurrences(settings.getMinimumOccurrences());

        if (!settings.isAllowInitialCapitals()) {
            builder = builder.excludeInitialCapital();
        }
        for (BaseListedFile file : settings.getFilterFiles()) {
            builder = addFilter(builder, file);
        }

        return builder.build();
    }

    private FilterBuilder addFilter(final FilterBuilder builder, final BaseListedFile file) {
        return builder.addExcludedWordsSupplier(() -> extractor.extract(file));
    }

    public void beginAsyncFiltering() {
        executor.beginExecution();
    }
}
