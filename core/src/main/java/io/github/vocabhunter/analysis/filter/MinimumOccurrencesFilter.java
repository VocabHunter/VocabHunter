/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import io.github.vocabhunter.analysis.model.AnalysisWord;

public final class MinimumOccurrencesFilter implements WordFilter {
    private final int minimum;

    public MinimumOccurrencesFilter(final int minimum) {
        this.minimum = minimum;
    }

    @Override
    public boolean isShown(final AnalysisWord word) {
        return word.getUseCount() >= minimum;
    }
}
