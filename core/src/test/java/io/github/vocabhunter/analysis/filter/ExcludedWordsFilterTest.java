/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import io.github.vocabhunter.analysis.model.AnalysisWord;
import io.github.vocabhunter.analysis.model.WordUse;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static io.github.vocabhunter.analysis.core.CollectionTool.listOf;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class ExcludedWordsFilterTest {
    private static final List<String> WORDS_1 = listOf(
        "apple", "Pear", "PEACH", "plum", "Cherry"
    );

    private static final List<String> WORDS_2 = listOf(
        "tomato", "Cabbage", "Courgette", "marrow"
    );

    private static final List<String> WORDS_3 = listOf(
        "Salmon", "hake", "Bass", "BREAM"
    );

    private static final List<AnalysisWord> ANALYSIS_WORDS =
        Stream.of(WORDS_1, WORDS_2, WORDS_3)
            .flatMap(List::stream)
            .map(String::toLowerCase)
            .map(ExcludedWordsFilterTest::word)
            .collect(toList());

    @Test
    public void testNoFilter() {
        WordFilter filter = new FilterBuilder().build();

        validate(filter, listOf(WORDS_1, WORDS_2, WORDS_3));
    }

    @Test
    public void testFilterOne() {
        WordFilter filter = new FilterBuilder()
            .addExcludedWords(WORDS_1)
            .build();

        validate(filter, listOf(WORDS_2, WORDS_3));
    }

    @Test
    public void testFilterTwo() {
        WordFilter filter = new FilterBuilder()
            .addExcludedWords(WORDS_1).addExcludedWords(WORDS_2)
            .build();

        validate(filter, listOf(WORDS_3));
    }

    @Test
    public void testFilterThree() {
        WordFilter filter = new FilterBuilder()
            .addExcludedWords(WORDS_1).addExcludedWords(WORDS_2).addExcludedWords(WORDS_3)
            .build();

        validate(filter, emptyList());
    }

    private void validate(final WordFilter filter, final List<List<String>> lists) {
        List<String> expected = lists.stream()
            .flatMap(List::stream)
            .map(String::toLowerCase)
            .collect(toList());
        List<String> actual = ANALYSIS_WORDS.stream()
            .filter(filter::isShown)
            .map(AnalysisWord::getWordIdentifier)
            .collect(toList());

        assertEquals("Filtered words", expected, actual);
    }

    private static AnalysisWord word(final String word) {
        return new WordUse(word, 1, emptyList());
    }
}
