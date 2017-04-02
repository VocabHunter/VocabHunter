/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.test.utils.TestFileManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SessionSerialiserTest {
    private TestFileManager files;

    private Path file;

    private final SessionState state1 = state("State 1");

    private final SessionState state2 = state("State 2");

    @Before
    public void setUp() throws Exception {
        files = new TestFileManager(getClass());
        file = files.addFile("file.json");
    }

    @After
    public void tearDown() throws Exception {
        files.cleanup();
    }

    @Test(expected = VocabHunterException.class)
    public void testNonexistentFile() {
        SessionSerialiser.read(Paths.get("does-not-exist.json"));
    }

    @Test(expected = VocabHunterException.class)
    public void testEmptyFile() throws Exception {
        Files.createFile(file);
        SessionSerialiser.read(file);
    }

    @Test(expected = VocabHunterException.class)
    public void testInvalidFile() throws Exception {
        Files.write(file, Collections.singletonList("unreadable"));
        SessionSerialiser.read(file);
    }

    @Test
    public void testSameState() throws Exception {
        SessionState read = writeAndReadBackState1();

        assertEquals("Same state", state1, read);
    }

    @Test
    public void testDifferentState() throws Exception {
        SessionState read = writeAndReadBackState1();

        assertNotEquals("Different state", state2, read);
    }

    private SessionState writeAndReadBackState1() {
        SessionSerialiser.write(file, state1);

        return SessionSerialiser.read(file).getState();
    }

    private SessionState state(final String name) {
        SessionState bean = new SessionState();
        SessionWord word1 = word("Word1", WordState.UNSEEN, 0, 1);
        SessionWord word2 = word("Word2", WordState.UNSEEN, 2, 3);

        bean.setName(name);
        bean.setOrderedUses(listOf(word1, word2));
        bean.setLines(listOf("Use 1", "Use 2", "Use 3", "Use 4"));

        return bean;
    }

    private SessionWord word(final String word, final WordState state, final Integer... lineNos) {
        SessionWord bean = new SessionWord();

        bean.setWordIdentifier(word);
        bean.setState(state);
        bean.setLineNos(listOf(lineNos));

        return bean;
    }
}
