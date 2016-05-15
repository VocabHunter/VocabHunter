/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.file;

import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.analysis.session.SessionWord;
import io.github.vocabhunter.test.utils.TestFileManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static io.github.vocabhunter.analysis.core.CollectionTool.listOf;
import static org.junit.Assert.assertEquals;

public class SelectionExportToolTest {
    private TestFileManager files;

    private Path file;

    @Before
    public void setUp() throws Exception {
        files = new TestFileManager(getClass());
        file = files.addFile("export.txt");
    }

    @After
    public void tearDown() throws Exception {
        files.cleanup();
    }

    @Test
    public void testEmpty() throws Exception {
        SessionState state = state();

        validate(state);
    }

    @Test
    public void testSelection() throws Exception {
        SessionState state = state(
                word("Out1", WordState.KNOWN),
                word("In1", WordState.UNKNOWN),
                word("Out2", WordState.UNSEEN),
                word("In2", WordState.UNKNOWN));

        validate(state, "In1", "In2");
    }

    private void validate(final SessionState state, final String... expected) throws Exception {
        SelectionExportTool.exportSelection(state, file);

        List<String> actual = Files.readAllLines(file);
        assertEquals("File content", listOf(expected), actual);
    }

    private SessionState state(final SessionWord... words) {
        SessionState bean = new SessionState();

        bean.setOrderedUses(listOf(words));

        return bean;
    }

    private SessionWord word(final String name, final WordState state) {
        SessionWord bean = new SessionWord();

        bean.setWordIdentifier(name);
        bean.setState(state);

        return bean;
    }
}
