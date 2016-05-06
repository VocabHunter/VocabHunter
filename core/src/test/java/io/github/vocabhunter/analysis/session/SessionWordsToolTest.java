/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.filter.FilterBuilder;
import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.analysis.marked.MarkTool;
import io.github.vocabhunter.analysis.model.AnalysisWord;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

import static io.github.vocabhunter.analysis.core.CollectionTool.listOf;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class SessionWordsToolTest {
    private static final String SESSION_FILE = "unmarked.wordy";

    private static final String FILTER_FILE = "format1.wordy";

    @Test
    public void testKnown() throws Exception {
        validate(SessionWordsTool::knownWords, listOf(
            "is", "a", "aid", "all", "back", "brown", "come", "document", "dog's", "for", "fox", "good", "jumped", "lazy",
            "men", "Now", "of", "over", "party", "quick", "simple", "test", "This", "time"));
    }

    @Test
    public void testSeen() throws Exception {
        validate(SessionWordsTool::seenWords, listOf(
            "aid", "all", "back", "brown", "come", "document", "dog's", "for", "fox", "good", "jumped", "lazy",
            "men", "Now", "of", "over", "party", "quick", "simple", "test", "This", "time"));
    }

    private void validate(final Function<EnrichedSessionState, List<String>> filterMethod, final List<String> expected) throws Exception {
        WordFilter filter = buildFilter(filterMethod);
        EnrichedSessionState state = read(SESSION_FILE);
        MarkTool<SessionWord> markTool = new MarkTool<>(filter, state.getState().getOrderedUses());
        List<SessionWord> words = markTool.getShownWords();
        List<String> actual = words.stream()
            .map(AnalysisWord::getWordIdentifier)
            .collect(toList());

        assertEquals("Filtered words", expected, actual);
    }

    private WordFilter buildFilter(final Function<EnrichedSessionState, List<String>> filterMethod) throws Exception {
        EnrichedSessionState state = read(FILTER_FILE);
        List<String> exclusions = filterMethod.apply(state);

        return new FilterBuilder().addExcludedWords(exclusions).build();
    }

    private EnrichedSessionState read(final String file) throws Exception {
        Path path = getResourceFile(file);

        return SessionSerialiser.read(path);
    }

    private Path getResourceFile(final String file) throws Exception {
        return Paths.get(SessionWordsToolTest.class.getResource("/" + file).toURI());
    }
}
