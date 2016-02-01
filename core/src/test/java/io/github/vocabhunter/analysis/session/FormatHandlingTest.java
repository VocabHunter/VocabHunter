package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.vocabhunter.analysis.session.WordState.*;
import static org.junit.Assert.assertEquals;

public class FormatHandlingTest {
    private static final String FORMAT_UNSUPPORTED_VERSION = "format-unsupported-version.wordy";

    private static final String FORMAT_1 = "format1.wordy";

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
    public void testVesion1() throws Exception {
        validate(FORMAT_1);
    }

    private void validate(final String filename) throws Exception {
        Path file = getResourceFile(filename);
        EnrichedSessionState expected = new EnrichedSessionState(EXPECTED_STATE, file);
        EnrichedSessionState actual = read(file);

        assertEquals("Session file", expected, actual);
    }

    private void validate(final SessionState state) {
        assertEquals("Document name", DOCUMENT_NAME, state.getName());
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


        use(uses, "the", KNOWN, LINE_1, LINE_2);
        use(uses, "is", UNKNOWN, LINE_2, LINE_3);
        use(uses, "to", KNOWN, LINE_2);
        use(uses, "aid", UNKNOWN, LINE_2);
        use(uses, "all", UNSEEN, LINE_2);
        use(uses, "back", UNSEEN, LINE_1);
        use(uses, "brown", UNSEEN, LINE_1);
        use(uses, "come", UNSEEN, LINE_2);
        use(uses, "document", UNSEEN, LINE_3);
        use(uses, "dog's", UNSEEN, LINE_1);
        use(uses, "for", UNSEEN, LINE_2);
        use(uses, "fox", UNSEEN, LINE_1);
        use(uses, "good", UNSEEN, LINE_2);
        use(uses, "jumped", UNSEEN, LINE_1);
        use(uses, "lazy", UNSEEN, LINE_1);
        use(uses, "men", UNSEEN, LINE_2);
        use(uses, "now", UNSEEN, LINE_2);
        use(uses, "of", UNSEEN, LINE_2);
        use(uses, "over", UNSEEN, LINE_1);
        use(uses, "party", UNSEEN, LINE_2);
        use(uses, "quick", UNSEEN, LINE_1);
        use(uses, "simple", UNSEEN, LINE_3);
        use(uses, "test", UNSEEN, LINE_3);
        use(uses, "this", UNSEEN, LINE_3);
        use(uses, "time", UNSEEN, LINE_2);

        state.setOrderedUses(uses);

        return state;
    }

    private static void use(final List<SessionWord> uses, final String word, final WordState state, final String... lines) {
        SessionWord sw = new SessionWord();

        sw.setWordIdentifier(word);
        sw.setState(state);
        sw.setUses(Arrays.asList(lines));

        uses.add(sw);
    }
}
