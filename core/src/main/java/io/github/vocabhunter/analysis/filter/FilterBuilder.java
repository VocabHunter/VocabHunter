/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterBuilder {
    private WordFilter minimumLettersFilter;

    private WordFilter minimumOccurrencesFilter;

    public FilterBuilder minimumLetters(final int count) {
        minimumLettersFilter = new MinimumLettersFilter(count);

        return this;
    }

    public FilterBuilder minimumOccurrences(final int count) {
        minimumOccurrencesFilter = new MinimumOccurrencesFilter(count);

        return this;
    }

    public WordFilter build() {
        List<WordFilter> filters = new ArrayList<>();

        if (minimumLettersFilter != null) {
            filters.add(minimumLettersFilter);
        }
        if (minimumOccurrencesFilter != null) {
            filters.add(minimumOccurrencesFilter);
        }

        return new AggregateFilter(filters);
    }
}
