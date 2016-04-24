/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FilterBuilder {
    private WordFilter minimumLettersFilter;

    private WordFilter minimumOccurrencesFilter;

    private WordFilter excludeInitialCapitalFilter;

    private final List<Collection<String>> excludedWords = new ArrayList<>();

    public FilterBuilder minimumLetters(final int count) {
        minimumLettersFilter = new MinimumLettersFilter(count);

        return this;
    }

    public FilterBuilder minimumOccurrences(final int count) {
        minimumOccurrencesFilter = new MinimumOccurrencesFilter(count);

        return this;
    }

    public FilterBuilder excludeInitialCapital() {
        excludeInitialCapitalFilter = new InitialCapitalFilter();

        return this;
    }

    public FilterBuilder addExcludedWords(final Collection<String> words) {
        excludedWords.add(words);

        return this;
    }

    public WordFilter build() {
        List<WordFilter> filters = new ArrayList<>();

        addIfUsed(filters, minimumLettersFilter);
        addIfUsed(filters, minimumOccurrencesFilter);
        addIfUsed(filters, excludeInitialCapitalFilter);
        if (!excludedWords.isEmpty()) {
            filters.add(new ExcludedWordsFilter(excludedWords));
        }

        return new AggregateFilter(filters);
    }

    private void addIfUsed(final List<WordFilter> filters, final WordFilter filter) {
        if (filter != null) {
            filters.add(filter);
        }
    }
}
