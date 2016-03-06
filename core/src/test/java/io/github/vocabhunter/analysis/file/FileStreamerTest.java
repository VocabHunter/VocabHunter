/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.file;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.model.Analyser;
import io.github.vocabhunter.analysis.session.EnrichedSessionState;
import io.github.vocabhunter.analysis.simple.SimpleAnalyser;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.vocabhunter.analysis.core.CollectionTool.listOf;
import static org.junit.Assert.assertEquals;

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

    @Test(expected = VocabHunterException.class)
    public void testStreamEmpty() throws Exception {
        validateStream(FILE_EMPTY);
    }

    @Test
    public void testStreamText() throws Exception {
        validateStream(FILE_TEXT);
    }

    @Test
    public void testStreamWord() throws Exception {
        validateStream(FILE_WORD);
    }

    @Test
    public void testStreamOpenOffice() throws Exception {
        validateStream(FILE_OPEN_OFFICE);
    }

    @Test
    public void testStreamPdf() throws Exception {
        validateStream(FILE_PDF);
    }

    @Test
    public void testCreateNewSessionFromText() throws Exception {
        validateSession(FILE_TEXT, FILE_TEXT, this::createNewSession);
    }

    @Test
    public void testCreateOrOpenSessionFromText() throws Exception {
        validateSession(FILE_TEXT, FILE_TEXT, this::createOrOpenSession);
    }

    @Test
    public void testCreateOrOpenSessionFromSession() throws Exception {
        validateSession(SESSION_FILE, SESSION_NAME, this::createOrOpenSession);
    }

    private void validateStream(final String file) throws Exception {
        URL resource = getResource(file);
        try (InputStream in = resource.openStream()) {
            List<String> result = target.stream(in, Paths.get(file))
                    .collect(Collectors.toList());

            assertEquals("Lines from file", LINES, result);
        }
    }

    private void validateSession(final String fileName, final String sessionName, final Function<Path, EnrichedSessionState> targetMethod) throws Exception {
        URL resource = getResource(fileName);
        Path file = Paths.get(resource.toURI());
        EnrichedSessionState session = targetMethod.apply(file);

        assertEquals("Session name", sessionName, session.getState().getName());
    }

    private EnrichedSessionState createNewSession(final Path file) {
        return target.createNewSession(file);
    }

    private EnrichedSessionState createOrOpenSession(final Path file) {
        return target.createOrOpenSession(file);
    }

    private URL getResource(final String file) {
        return FileStreamerTest.class.getResource("/" + file);
    }
}
