/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import io.github.vocabhunter.analysis.model.AnalysisWord;

import java.util.ArrayList;
import java.util.List;

public final class AggregateFilter implements WordFilter {
    private final List<WordFilter> filters;

    public AggregateFilter(final List<WordFilter> filters) {
        this.filters = new ArrayList<>(filters);
    }

    @Override
    public boolean isShown(final AnalysisWord word) {
        for (WordFilter filter : filters) {
            if (!filter.isShown(word)) {
                return false;
            }
        }

        return true;
    }
}
