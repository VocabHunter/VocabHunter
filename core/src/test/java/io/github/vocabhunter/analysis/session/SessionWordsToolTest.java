/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.filter.FilterBuilder;
import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.analysis.marked.MarkTool;
import io.github.vocabhunter.analysis.marked.MarkedWord;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SessionWordsToolTest {
    private static final String SESSION_FILE = "unmarked.wordy";

    private static final String FILTER_FILE = "format1.wordy";

    private final SessionWordsTool target = new SessionWordsToolImpl();

    @Test
    public void testKnown() throws Exception {
        validate(target::knownWords, listOf(
            "is", "a", "aid", "all", "back", "brown", "come", "document", "dog's", "for", "fox", "good", "jumped", "lazy",
            "men", "Now", "of", "over", "party", "quick", "simple", "test", "This", "time"));
    }

    @Test
    public void testSeen() throws Exception {
        validate(target::seenWords, listOf(
            "aid", "all", "back", "brown", "come", "document", "dog's", "for", "fox", "good", "jumped", "lazy",
            "men", "Now", "of", "over", "party", "quick", "simple", "test", "This", "time"));
    }

    private void validate(final Function<Path, List<String>> filterMethod, final List<String> expected) throws Exception {
        WordFilter filter = buildFilter(filterMethod);
        List<? extends MarkedWord> words = read(SESSION_FILE);
        MarkTool<? extends MarkedWord> markTool = new MarkTool<>(filter, words);
        List<? extends MarkedWord> shownWords = markTool.getShownWords();
        List<String> actual = shownWords.stream()
            .map(MarkedWord::getWordIdentifier)
            .collect(toList());

        assertEquals(expected, actual, "Filtered words");
    }

    private WordFilter buildFilter(final Function<Path, List<String>> filterMethod) throws Exception {
        Path file = getResourceFile(FILTER_FILE);
        List<String> exclusions = filterMethod.apply(file);

        return new FilterBuilder().addExcludedWords(exclusions).build();
    }

    private List<? extends MarkedWord> read(final String file) throws Exception {
        Path path = getResourceFile(file);

        return SessionSerialiser.readMarkedWords(path);
    }

    private Path getResourceFile(final String file) throws Exception {
        return Paths.get(SessionWordsToolTest.class.getResource("/" + file).toURI());
    }
}
