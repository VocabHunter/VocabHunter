/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import io.github.vocabhunter.analysis.model.AnalysisWord;
import io.github.vocabhunter.analysis.model.WordUse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordFilterTest {
    private static final List<AnalysisWord> WORDS = List.of(
        word("A", 10),
        word("don't", 2),
        word("Hello", 2),
        word("banana", 3)
    );

    @Test
    public void testNoFilter() {
        WordFilter filter = new FilterBuilder().build();

        validate(filter, "A", "don't", "Hello", "banana");
    }

    @Test
    public void testMinimumOccurrences() {
        WordFilter filter = new FilterBuilder().minimumOccurrences(3).build();

        validate(filter, "A", "banana");
    }

    @Test
    public void testMinimumLetters() {
        WordFilter filter = new FilterBuilder().minimumLetters(5).build();

        validate(filter, "Hello", "banana");
    }

    @Test
    public void testMinimumOccurrencesAndMinimumLetters() {
        WordFilter filter = new FilterBuilder().minimumOccurrences(3).minimumLetters(2).build();

        validate(filter, "banana");
    }

    @Test
    public void testExcludeInitialCapital() {
        WordFilter filter = new FilterBuilder().excludeInitialCapital().build();

        validate(filter, "don't", "banana");
    }

    @Test
    public void testExcludeInitialCapitalAndMinimumOccurrences() {
        WordFilter filter = new FilterBuilder().excludeInitialCapital().minimumOccurrences(3).build();

        validate(filter, "banana");
    }

    private void validate(final WordFilter filter, final String... expected) {
        List<String> actual = WORDS.stream()
            .filter(filter::isShown)
            .map(AnalysisWord::getWordIdentifier)
            .toList();

        assertEquals(List.of(expected), actual, "Filtered words");
    }

    private static AnalysisWord word(final String word, final int uses) {
        return new WordUse(word, uses, List.of());
    }
}
