/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.file;

import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.session.EnrichedSessionState;
import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.analysis.session.SessionWord;
import io.github.vocabhunter.analysis.simple.SimpleAnalyser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnalysisSystemTest {
    private static final String INPUT_DOCUMENT = "bleak-house.txt";

    private static List<SessionWord> words;

    @BeforeAll
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
    public void testWordThe() {
        validate("the", 14922, 8325);
    }

    @Test
    public void testWordLondon() {
        validate("London", 83, 83);
    }

    private void validate(final String identifier, final int useCount, final int lineCount) {
        List<SessionWord> found = words.stream()
            .filter(w -> w.getWordIdentifier().equalsIgnoreCase(identifier))
            .collect(Collectors.toList());

        assertAll(
            () -> assertEquals(1, found.size(), "List size"),
            () -> {
                SessionWord word = found.get(0);

                assertAll(
                    () -> assertEquals(identifier, word.getWordIdentifier(), "Word"),
                    () -> assertEquals(useCount, word.getUseCount(), "Use count"),
                    () -> validateLines(word.getLineNos(), lineCount)
                );
            }
        );
    }

    private void validateLines(final List<Integer> lines, final int lineCount) {
        assertAll(
            () -> assertEquals(lineCount, lines.size(), "Line number count"),
            () -> {
                List<Integer> orderedDistinct = lines.stream()
                    .sorted()
                    .distinct()
                    .collect(Collectors.toList());

                assertEquals(orderedDistinct, lines, "Distinct ordered lines");
            }
        );
    }
}
