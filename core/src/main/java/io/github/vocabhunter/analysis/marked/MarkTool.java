/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.marked;

import io.github.vocabhunter.analysis.filter.WordFilter;

import java.util.List;
import java.util.stream.Collectors;

public final class MarkTool<T extends MarkedWord> {
    private final List<T> filteredWords;

    public MarkTool(final List<T> words) {
        filteredWords = words;
    }

    public MarkTool(final WordFilter filter, final List<T> words) {
        filteredWords = words.stream()
            .filter(filter::isShown)
            .collect(Collectors.toList());
    }

    public List<T> getFilteredWords() {
        return filteredWords;
    }

    public boolean isValidFilter() {
        return !filteredWords.isEmpty();
    }

}
