/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import io.github.vocabhunter.analysis.core.CoreTool;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.model.AnalysisWord;
import io.github.vocabhunter.analysis.model.WordUse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ExcludedWordsFilterTest {
    private static final String ERROR_MESSAGE = "ERROR_MESSAGE";

    private static final List<String> WORDS_1 = List.of(
        "apple", "Pear", "PEACH", "plum", "Cherry"
    );

    private static final List<String> WORDS_2 = List.of(
        "tomato", "Cabbage", "Courgette", "marrow"
    );

    private static final List<String> WORDS_3 = List.of(
        "Salmon", "hake", "Bass", "BREAM"
    );

    private static final List<AnalysisWord> ANALYSIS_WORDS =
        Stream.of(WORDS_1, WORDS_2, WORDS_3)
            .flatMap(List::stream)
            .map(CoreTool::toLowerCase)
            .map(ExcludedWordsFilterTest::word)
            .toList();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @AfterEach
    public void tearDown() {
        executorService.shutdown();
    }

    @Test
    public void testNoFilter() {
        WordFilter filter = new FilterBuilder().build();

        validate(filter, List.of(WORDS_1, WORDS_2, WORDS_3));
    }

    @Test
    public void testFilterOne() {
        WordFilter filter = new FilterBuilder()
            .addExcludedWords(WORDS_1)
            .build();

        validate(filter, List.of(WORDS_2, WORDS_3));
    }

    @Test
    public void testFilterTwo() {
        WordFilter filter = new FilterBuilder()
            .addExcludedWords(WORDS_1).addExcludedWords(WORDS_2)
            .build();

        validate(filter, List.of(WORDS_3));
    }

    @Test
    public void testFilterThree() {
        WordFilter filter = new FilterBuilder()
            .addExcludedWords(WORDS_1).addExcludedWords(WORDS_2).addExcludedWords(WORDS_3)
            .build();

        validate(filter, List.of());
    }

    @Test
    public void testFilterAsync() {
        WordFilter filter = new FilterBuilder()
            .executor(executorService)
            .addExcludedWordsSupplier(() -> WORDS_1).addExcludedWordsSupplier(() -> WORDS_2)
            .build();

        validate(filter, List.of(WORDS_3));
    }

    @Test
    public void testAsyncException() {
        WordFilter filter = new FilterBuilder()
            .executor(executorService)
            .addExcludedWordsSupplier(this::throwException)
            .build();

        try {
            filter.isShown(word("test"));
            fail("No exception thrown");
        } catch (final VocabHunterException e) {
            assertEquals(ERROR_MESSAGE, e.getMessage());
        }
    }

    private List<String> throwException() {
        throw new VocabHunterException(ERROR_MESSAGE);
    }

    private void validate(final WordFilter filter, final List<List<String>> lists) {
        List<String> expected = lists.stream()
            .flatMap(List::stream)
            .map(CoreTool::toLowerCase)
            .toList();
        List<String> actual = ANALYSIS_WORDS.stream()
            .filter(filter::isShown)
            .map(AnalysisWord::getWordIdentifier)
            .toList();

        assertEquals(expected, actual, "Filtered words");
    }

    private static AnalysisWord word(final String word) {
        return new WordUse(word, 1, List.of());
    }
}
