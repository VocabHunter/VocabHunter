/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.marked.MarkedWord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static io.github.vocabhunter.analysis.session.TestSessionStateTool.buildSession;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

public class FormatHandlingTest {
    private static final String FORMAT_UNSUPPORTED_VERSION = "format-unsupported-version.wordy";

    private static final String FORMAT_1 = "format1.wordy";

    private static final String FORMAT_2 = "format2.wordy";

    private static final String FORMAT_3 = "format3.wordy";

    private static final String FORMAT_4 = "format4.wordy";

    private static final String FORMAT_5 = "format5.wordy";

    private static final String FORMAT_UNEXPECTED_FIELD = "unexpected-field.wordy";

    private static final SessionState EXPECTED_STATE = buildSession();

    @Test
    public void testUnsupportedVersion() {
        assertThrows(VocabHunterException.class, () -> readState(FORMAT_UNSUPPORTED_VERSION));
    }

    @ParameterizedTest
    @ValueSource(strings = {FORMAT_1, FORMAT_2, FORMAT_3, FORMAT_4, FORMAT_5, FORMAT_UNEXPECTED_FIELD})
    public void testSupportedVersion(final String filename) throws Exception {
        Path file = getResourceFile(filename);
        EnrichedSessionState expected = new EnrichedSessionState(EXPECTED_STATE, file);

        assertAll(
            () -> validateMarkedWords(file, expected),
            () -> validateState(file, expected)
        );
    }

    private void validateState(final Path file, final EnrichedSessionState expected) {
        EnrichedSessionState actual = readState(file);

        assertAll(
            () -> {
                Optional<Path> expectedFile = expected.getFile();
                Optional<Path> actualFile = actual.getFile();
                assertEquals(expectedFile, actualFile, "Session file reference");
            },
            () -> {
                SessionState expectedState = expected.getState();
                SessionState actualState = actual.getState();

                assertAll(
                    () -> {
                        int expectedFormatVersion = expectedState.getFormatVersion();
                        int actualFormatVersion = actualState.getFormatVersion();
                        assertEquals(expectedFormatVersion, actualFormatVersion, "Format version");
                    },
                    () -> {
                        String expectedName = expectedState.getName();
                        String actualName = actualState.getName();
                        assertEquals(expectedName, actualName, "Session name");
                    },
                    () -> assertTrue(expectedState.isEquivalent(actualState), "Equivalent")
                );
            },
            // This catch-all case should already be covered
            () -> assertEquals(expected, actual, "Session file")
        );
    }

    private void validateMarkedWords(final Path file, final EnrichedSessionState expected) {
        List<SessionWord> expectedWords = expected.getState().getOrderedUses();
        List<? extends MarkedWord> actualWords = SessionSerialiser.readMarkedWords(file);

        assertAll(
            () -> validateMarkedWords(expectedWords, actualWords, MarkedWord::getWordIdentifier),
            () -> validateMarkedWords(expectedWords, actualWords, MarkedWord::getState),
            () -> validateMarkedWords(expectedWords, actualWords, MarkedWord::getUseCount)
        );
    }

    private void validateMarkedWords(final List<? extends MarkedWord> expected, final List<? extends MarkedWord> actual, final Function<MarkedWord, Object> f) {
        List<Object> expectedValues = expected.stream()
            .map(f)
            .collect(toList());
        List<Object> actualValues = actual.stream()
            .map(f)
            .collect(toList());

        assertEquals(expectedValues, actualValues, "Values");
    }

    private EnrichedSessionState readState(final String file) throws Exception {
        return readState(getResourceFile(file));
    }

    private EnrichedSessionState readState(final Path file) {
        return SessionSerialiser.read(file);
    }

    private Path getResourceFile(final String file) throws Exception {
        return Paths.get(FormatHandlingTest.class.getResource("/" + file).toURI());
    }
}
