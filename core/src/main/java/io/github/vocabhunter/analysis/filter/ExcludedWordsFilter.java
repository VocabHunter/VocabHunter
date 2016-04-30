/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import io.github.vocabhunter.analysis.model.AnalysisWord;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ExcludedWordsFilter implements WordFilter {
    private final Set<String> exclusions;

    public ExcludedWordsFilter(final Collection<Collection<String>> words) {
        exclusions = words.stream()
            .flatMap(Collection::stream)
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean isShown(final AnalysisWord word) {
        String identifier = word.getWordIdentifier().toLowerCase();

        return !exclusions.contains(identifier);
    }
}
