/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class FilterBuilder {
    private Executor executor;

    private WordFilter minimumLettersFilter;

    private WordFilter minimumOccurrencesFilter;

    private WordFilter excludeInitialCapitalFilter;

    private final List<Supplier<Collection<String>>> excludedWordsSuppliers = new ArrayList<>();

    public FilterBuilder executor(final Executor executor) {
        this.executor = executor;

        return this;
    }

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
        return addExcludedWordsSupplier(() -> words);
    }

    public FilterBuilder addExcludedWordsSupplier(final Supplier<Collection<String>> wordsSupplier) {
        excludedWordsSuppliers.add(wordsSupplier);

        return this;
    }

    public WordFilter build() {
        List<WordFilter> filters = new ArrayList<>();

        addIfUsed(filters, minimumLettersFilter);
        addIfUsed(filters, minimumOccurrencesFilter);
        addIfUsed(filters, excludeInitialCapitalFilter);

        if (!excludedWordsSuppliers.isEmpty()) {
            filters.add(new ExcludedWordsFilter(getExecutor(), excludedWordsSuppliers));
        }

        return new AggregateFilter(filters);
    }

    private Executor getExecutor() {
        if (executor == null) {
            return Runnable::run;
        } else {
            return executor;
        }
    }

    private void addIfUsed(final List<WordFilter> filters, final WordFilter filter) {
        if (filter != null) {
            filters.add(filter);
        }
    }
}
