/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.marked.WordState;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.vocabhunter.analysis.core.CollectionTool.listOf;
import static io.github.vocabhunter.analysis.marked.WordState.*;
import static org.junit.Assert.assertEquals;

public class FormatHandlingTest {
    private static final String FORMAT_UNSUPPORTED_VERSION = "format-unsupported-version.wordy";

    private static final String FORMAT_1 = "format1.wordy";

    private static final String FORMAT_2 = "format2.wordy";

    private static final String FORMAT_3 = "format3.wordy";

    private static final String DOCUMENT_NAME = "test-sample.txt";

    private static final String LINE_1 = "The quick brown fox jumped over the lazy dog's back.";

    private static final String LINE_2 = "Now is the time for all good men to come to the aid of the party.";

    private static final String LINE_3 = "This is a simple test document.";

    private static final SessionState EXPECTED_STATE = buildExpected();

    @Test(expected = VocabHunterException.class)
    public void testUnsupportedVersion() throws Exception {
        read(FORMAT_UNSUPPORTED_VERSION);
    }

    @Test
    public void testVersion1() throws Exception {
        validate(FORMAT_1);
    }

    @Test
    public void testVersion2() throws Exception {
        validate(FORMAT_2);
    }

    @Test
    public void testVersion3() throws Exception {
        validate(FORMAT_3);
    }

    private void validate(final String filename) throws Exception {
        Path file = getResourceFile(filename);
        EnrichedSessionState expected = new EnrichedSessionState(EXPECTED_STATE, file);
        EnrichedSessionState actual = read(file);

        validate(expected, actual);
    }

    private void validate(final EnrichedSessionState expected, final EnrichedSessionState actual) {
        Optional<Path> expectedFile = expected.getFile();
        Optional<Path> actualFile = actual.getFile();
        assertEquals("Session file reference", expectedFile, actualFile);

        SessionState expectedState = expected.getState();
        SessionState actualState = actual.getState();

        int expectedFormatVersion = expectedState.getFormatVersion();
        int actualFormatVersion = actualState.getFormatVersion();
        assertEquals("Format version", expectedFormatVersion, actualFormatVersion);

        String expectedName = expectedState.getName();
        String actualName = actualState.getName();
        assertEquals("Session name", expectedName, actualName);

        List<SessionWord> expectedUses = expectedState.getOrderedUses();
        List<SessionWord> actualUses = actualState.getOrderedUses();
        assertEquals("Words", expectedUses, actualUses);

        // This catch-all case should already be covered
        assertEquals("Session file", expected, actual);
    }

    private EnrichedSessionState read(final String file) throws Exception {
        return read(getResourceFile(file));
    }

    private EnrichedSessionState read(final Path file) {
        return SessionSerialiser.read(file);
    }

    private Path getResourceFile(final String file) throws Exception {
        return Paths.get(FormatHandlingTest.class.getResource("/" + file).toURI());
    }

    private static SessionState buildExpected() {
        SessionState state = new SessionState();

        state.setName(DOCUMENT_NAME);
        List<SessionWord> uses = new ArrayList<>();

        use(uses, "the", KNOWN, 5, LINE_1, LINE_2);
        use(uses, "is", UNKNOWN, 2, LINE_2, LINE_3);
        use(uses, "to", KNOWN, 2, LINE_2);
        use(uses, "a", UNKNOWN, 1, LINE_3);
        use(uses, "aid", UNSEEN, 1, LINE_2);
        use(uses, "all", UNSEEN, 1, LINE_2);
        use(uses, "back", UNSEEN, 1, LINE_1);
        use(uses, "brown", UNSEEN, 1, LINE_1);
        use(uses, "come", UNSEEN, 1, LINE_2);
        use(uses, "document", UNSEEN, 1, LINE_3);
        use(uses, "dog's", UNSEEN, 1, LINE_1);
        use(uses, "for", UNSEEN, 1, LINE_2);
        use(uses, "fox", UNSEEN, 1, LINE_1);
        use(uses, "good", UNSEEN, 1, LINE_2);
        use(uses, "jumped", UNSEEN, 1, LINE_1);
        use(uses, "lazy", UNSEEN, 1, LINE_1);
        use(uses, "men", UNSEEN, 1, LINE_2);
        use(uses, "Now", UNSEEN, 1, LINE_2);
        use(uses, "of", UNSEEN, 1, LINE_2);
        use(uses, "over", UNSEEN, 1, LINE_1);
        use(uses, "party", UNSEEN, 1, LINE_2);
        use(uses, "quick", UNSEEN, 1, LINE_1);
        use(uses, "simple", UNSEEN, 1, LINE_3);
        use(uses, "test", UNSEEN, 1, LINE_3);
        use(uses, "This", UNSEEN, 1, LINE_3);
        use(uses, "time", UNSEEN, 1, LINE_2);

        state.setOrderedUses(uses);

        return state;
    }

    private static void use(final List<SessionWord> uses, final String word, final WordState state, final int useCount, final String... lines) {
        SessionWord sw = new SessionWord();

        sw.setWordIdentifier(word);
        sw.setState(state);
        sw.setUses(listOf(lines));
        sw.setUseCount(useCount);

        uses.add(sw);
    }
}
