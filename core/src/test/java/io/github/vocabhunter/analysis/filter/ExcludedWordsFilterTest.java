/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.filter;

import io.github.vocabhunter.analysis.core.CoreTool;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.model.AnalysisWord;
import io.github.vocabhunter.analysis.model.WordUse;
import org.junit.After;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExcludedWordsFilterTest {
    private static final String ERROR_MESSAGE = "ERROR_MESSAGE";

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
            .map(CoreTool::toLowerCase)
            .map(ExcludedWordsFilterTest::word)
            .collect(toList());

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @After
    public void tearDown() {
        executorService.shutdown();
    }

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

    @Test
    public void testFilterAsync() {
        WordFilter filter = new FilterBuilder()
            .executor(executorService)
            .addExcludedWordsSupplier(() -> WORDS_1).addExcludedWordsSupplier(() -> WORDS_2)
            .build();

        validate(filter, listOf(WORDS_3));
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
        } catch (VocabHunterException e) {
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
