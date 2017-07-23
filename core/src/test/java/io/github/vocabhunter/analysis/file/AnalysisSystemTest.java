/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.file;

import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.session.EnrichedSessionState;
import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.analysis.session.SessionWord;
import io.github.vocabhunter.analysis.simple.SimpleAnalyser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class AnalysisSystemTest {
    private static final String INPUT_DOCUMENT = "bleak-house.txt";

    private static List<SessionWord> words;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Analyser analyser = new SimpleAnalyser();
        FileStreamer target = new FileStreamer(analyser);
        URL resource = FileStreamerTest.class.getResource("/" + INPUT_DOCUMENT);
        Path file = Paths.get(resource.toURI());
        EnrichedSessionState enrichedSession = target.createNewSession(file);
        SessionState sessionState = enrichedSession.getState();

        words = sessionState.getOrderedUses();
    }

    @Test
    public void testWordThe() throws Exception {
        validate("the", 14922, 8325);
    }

    @Test
    public void testWordLondon() throws Exception {
        validate("London", 83, 83);
    }

    private void validate(final String identifier, final int useCount, final int lineCount) {
        List<SessionWord> found = words.stream()
            .filter(w -> w.getWordIdentifier().equalsIgnoreCase(identifier))
            .collect(Collectors.toList());

        assertEquals("List size", 1, found.size());

        SessionWord word = found.get(0);

        assertEquals("Word", identifier, word.getWordIdentifier());
        assertEquals("Use count", useCount, word.getUseCount());
        validateLines(word.getLineNos(), lineCount);
    }

    private void validateLines(final List<Integer> lines, final int lineCount) {
        assertEquals("Line number count", lineCount, lines.size());

        List<Integer> orderedDistinct = lines.stream()
            .sorted()
            .distinct()
            .collect(Collectors.toList());

        assertEquals("Distinct ordered lines", orderedDistinct, lines);
    }
}
