/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.test.utils.TestFileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SessionSerialiserTest {
    private TestFileManager files;

    private Path file;

    private final SessionState state1 = state("State 1");

    private final SessionState state2 = state("State 2");

    @BeforeEach
    public void setUp() throws Exception {
        files = new TestFileManager(getClass());
        file = files.addFile("file.json");
    }

    @AfterEach
    public void tearDown() throws Exception {
        files.cleanup();
    }

    @Test
    public void testNonexistentFile() {
        assertThrows(VocabHunterException.class, () -> SessionSerialiser.read(Paths.get("does-not-exist.json")));
    }

    @Test
    public void testEmptyFile() throws Exception {
        Files.createFile(file);

        assertThrows(VocabHunterException.class, () -> SessionSerialiser.read(file));
    }

    @Test
    public void testInvalidFile() throws Exception {
        Files.write(file, Collections.singletonList("unreadable"));

        assertThrows(VocabHunterException.class, () -> SessionSerialiser.read(file));
    }

    @Test
    public void testSameState() {
        SessionState read = writeAndReadBackState1();

        assertEquals(state1, read, "Same state");
    }

    @Test
    public void testDifferentState() {
        SessionState read = writeAndReadBackState1();

        assertNotEquals(state2, read, "Different state");
    }

    private SessionState writeAndReadBackState1() {
        SessionSerialiser.write(file, state1);

        return SessionSerialiser.read(file).getState();
    }

    private SessionState state(final String name) {
        SessionState bean = new SessionState();
        SessionWord word1 = word("Word1", WordState.UNSEEN, "My Note", 0, 1);
        SessionWord word2 = word("Word2", WordState.UNSEEN, null, 2, 3);

        bean.setName(name);
        bean.setOrderedUses(listOf(word1, word2));
        bean.setLines(listOf("Use 1", "Use 2", "Use 3", "Use 4"));

        return bean;
    }

    private SessionWord word(final String word, final WordState state, final String note, final Integer... lineNos) {
        SessionWord bean = new SessionWord();

        bean.setWordIdentifier(word);
        bean.setState(state);
        bean.setNote(note);
        bean.setLineNos(listOf(lineNos));

        return bean;
    }
}
