/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.file;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.session.EnrichedSessionState;
import io.github.vocabhunter.analysis.simple.SimpleAnalyser;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static org.junit.jupiter.api.Assertions.*;

public class FileStreamerTest {
    private static final List<String> LINES = listOf(
            "The quick brown fox jumped over the lazy dog's back.",
            "Now is the time for all good men to come to the aid of the party.",
            "This is a simple test document.");

    private static final String FILE_EMPTY = "empty.txt";

    private static final String FILE_TEXT = "sample.txt";

    private static final String FILE_WORD = "sample.doc";

    private static final String FILE_OPEN_OFFICE = "sample.odt";

    private static final String FILE_PDF = "sample.pdf";

    private static final String SESSION_FILE = "format1.wordy";

    private static final String SESSION_NAME = "test-sample.txt";

    private final Analyser analyser = new SimpleAnalyser();

    private final FileStreamer target = new FileStreamer(analyser);

    @Test
    public void testStreamEmpty() {
        assertThrows(VocabHunterException.class, () -> validateStream(FILE_EMPTY));
    }

    @Test
    public void testEqualSession() {
        EnrichedSessionState state1 = createNewSession(FILE_TEXT);
        EnrichedSessionState state2 = createNewSession(FILE_TEXT);

        assertEquals(state1, state2, "Equal session");
    }

    @Test
    public void testDifferentSession() {
        EnrichedSessionState state1 = createNewSession(FILE_TEXT);
        EnrichedSessionState state2 = createNewSession(FILE_WORD);

        assertNotEquals(state1, state2, "Different session");
    }

    @Test
    public void testEqualHashCode() {
        EnrichedSessionState state1 = createNewSession(FILE_TEXT);
        EnrichedSessionState state2 = createNewSession(FILE_TEXT);

        assertEquals(state1.hashCode(), state2.hashCode(), "Equal hash code session");
    }

    @Test
    public void testSessionToString() {
        EnrichedSessionState state = createNewSession(FILE_TEXT);

        assertTrue(StringUtils.isNotBlank(state.toString()), "Session toString");
    }

    @Test
    public void testStreamText() {
        validateStream(FILE_TEXT);
    }

    @Test
    public void testStreamWord() {
        validateStream(FILE_WORD);
    }

    @Test
    public void testStreamOpenOffice() {
        validateStream(FILE_OPEN_OFFICE);
    }

    @Test
    public void testStreamPdf() {
        validateStream(FILE_PDF);
    }

    @Test
    public void testCreateNewSessionFromText() {
        validateSession(FILE_TEXT, FILE_TEXT, this::createNewSession);
    }

    @Test
    public void testCreateOrOpenSessionFromText() {
        validateSession(FILE_TEXT, FILE_TEXT, this::createOrOpenSession);
    }

    @Test
    public void testCreateOrOpenSessionFromSession() {
        validateSession(SESSION_FILE, SESSION_NAME, this::createOrOpenSession);
    }

    private void validateStream(final String file) {
        List<String> result = target.lines(getFile(file));

        assertEquals(LINES, result, "Lines from file");
    }

    private void validateSession(final String fileName, final String sessionName, final Function<String, EnrichedSessionState> targetMethod) {
        EnrichedSessionState session = targetMethod.apply(fileName);

        assertEquals(sessionName, session.getState().getName(), "Session name");
    }

    private EnrichedSessionState createNewSession(final String file) {
        return target.createNewSession(getFile(file));
    }

    private EnrichedSessionState createOrOpenSession(final String file) {
        return target.createOrOpenSession(getFile(file));
    }

    private Path getFile(final String fileName) {
        try {
            URL resource = getResource(fileName);

            return Paths.get(resource.toURI());
        } catch (final URISyntaxException e) {
            throw new VocabHunterException("Filename error", e);
        }
    }

    private URL getResource(final String file) {
        return FileStreamerTest.class.getResource("/" + file);
    }
}
