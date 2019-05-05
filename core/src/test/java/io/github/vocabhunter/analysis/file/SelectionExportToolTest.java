/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.file;

import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.analysis.session.SessionWord;
import io.github.vocabhunter.test.utils.TestFileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelectionExportToolTest {
    private TestFileManager files;

    private Path file;

    private final SessionState emptyState = state();

    private final SessionState state = state(
        word("Out1", WordState.KNOWN),
        word("In1",  WordState.UNKNOWN, "Simple Note"),
        word("Out2", WordState.UNSEEN, "Ignored Note"),
        word("In2",  WordState.UNKNOWN, "Line 1\nLine 2"),
        word("In3",  WordState.UNKNOWN),
        word("In4",  WordState.UNKNOWN, "\t\n Spaced Note\n \n"),
        word("In5",  WordState.UNKNOWN, "\n Line 1 \n \n Line 3 "),
        word("In6",  WordState.UNKNOWN, " ")
    );

    @BeforeEach
    public void setUp() throws Exception {
        files = new TestFileManager(getClass());
        file = files.addFile("export.txt");
    }

    @AfterEach
    public void tearDown() throws Exception {
        files.cleanup();
    }

    @Test
    public void testEmpty() throws Exception {
        validate(emptyState, false);
    }

    @Test
    public void testSelectionWithoutNotes() throws Exception {
        validate(state, false, "In1", "In2", "In3", "In4", "In5", "In6");
    }

    @Test
    public void testSelectionWithNotes() throws Exception {
        validate(state, true, "In1\tSimple Note", "In2\tLine 1", "\tLine 2", "In3", "In4\tSpaced Note", "In5\tLine 1", "", "\tLine 3", "In6");
    }

    private void validate(final SessionState state, final boolean isNoteIncluded, final String... expected) throws Exception {
        SelectionExportTool.exportSelection(state, file, isNoteIncluded);

        List<String> actual = Files.readAllLines(file);
        assertEquals(List.of(expected), actual, "File content");
    }

    private SessionState state(final SessionWord... words) {
        SessionState bean = new SessionState();

        bean.setOrderedUses(List.of(words));

        return bean;
    }

    private SessionWord word(final String name, final WordState state) {
        return word(name, state, null);
    }

    private SessionWord word(final String name, final WordState state, final String note) {
        SessionWord bean = new SessionWord();

        bean.setWordIdentifier(name);
        bean.setState(state);
        bean.setNote(note);

        return bean;
    }
}
