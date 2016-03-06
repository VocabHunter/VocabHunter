/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import io.github.vocabhunter.analysis.model.AnalysisWord;
import io.github.vocabhunter.analysis.model.WordUse;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static io.github.vocabhunter.analysis.core.CollectionTool.listOf;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class WordFilterTest {
    private static final List<AnalysisWord> WORDS = listOf(
        word("a", 10),
        word("don't", 2),
        word("hello", 2),
        word("banana", 3)
    );

    @Test
    public void testNoFilter() {
        WordFilter filter = new FilterBuilder().build();

        validate(filter, "a", "don't", "hello", "banana");
    }

    @Test
    public void testMinimumOccurrences() {
        WordFilter filter = new FilterBuilder().minimumOccurrences(3).build();

        validate(filter, "a", "banana");
    }

    @Test
    public void testMinimumLetters() {
        WordFilter filter = new FilterBuilder().minimumLetters(5).build();

        validate(filter, "hello", "banana");
    }

    @Test
    public void testMinimumOccurrencesAndMinumumLetters() {
        WordFilter filter = new FilterBuilder().minimumOccurrences(3).minimumLetters(2).build();

        validate(filter, "banana");
    }

    private void validate(final WordFilter filter, final String... expected) {
        List<String> actual = WORDS.stream()
            .filter(filter::isShown)
            .map(AnalysisWord::getWordIdentifier)
            .collect(toList());

        assertEquals("Filtered words", listOf(expected), actual);
    }

    private static AnalysisWord word(final String word, final int uses) {
        return new WordUse(word, uses, Collections.emptyList());
    }
}
