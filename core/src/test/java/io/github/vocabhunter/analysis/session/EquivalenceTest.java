/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.core.CoreTool;
import io.github.vocabhunter.analysis.marked.WordState;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.vocabhunter.analysis.session.TestSessionStateTool.*;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class EquivalenceTest {
    public final SessionState state1 = buildSession();

    @Test
    public void testIdentical() {
        SessionState state2 = buildSession();

        validateEquivalent(state2);
    }

    @Test
    public void testDifferentName() {
        SessionState state2 = buildSession();
        state2.setName("Different");

        validateNotEquivalent(state2);
    }

    @Test
    public void testLinesOrder() {
        SessionState state2 = buildSession(CoreTool.listOf(LINE_3, LINE_1, LINE_2));

        validateEquivalent(state2);
    }

    @Test
    public void testDuplicateLines() {
        SessionState state2 = buildSession(CoreTool.listOf(LINE_1, LINE_2, LINE_3, LINE_1));

        validateEquivalent(state2);
    }

    @Test
    public void testExtraLine() {
        SessionState state2 = buildSession(CoreTool.listOf(LINE_1, LINE_2, LINE_3, "Extra"));

        validateNotEquivalent(state2);
    }

    @Test
    public void testMissingWord() {
        SessionState state2 = buildSession();
        List<SessionWord> words = state2.getOrderedUses();

        words = words.subList(0, words.size() - 1);
        state2.setOrderedUses(words);

        validateNotEquivalent(state2);
    }

    @Test
    public void testDifferentWordIdentifier() {
        SessionState state2 = buildSession();
        SessionWord word = state2.getOrderedUses().get(0);

        word.setWordIdentifier("Unexpected");

        validateNotEquivalent(state2);
    }

    @Test
    public void testDifferentUseCount() {
        SessionState state2 = buildSession();
        SessionWord word = state2.getOrderedUses().get(0);

        word.setUseCount(word.getUseCount() + 1);

        validateNotEquivalent(state2);
    }

    @Test
    public void testDifferentWordState() {
        SessionState state2 = buildSession();
        SessionWord word = state2.getOrderedUses().get(0);

        word.setState(WordState.UNSEEN);

        validateNotEquivalent(state2);
    }

    @Test
    public void testExtraLineNo() {
        SessionState state2 = buildSession();
        SessionWord word = state2.getOrderedUses().get(0);
        List<Integer> lineNos = new ArrayList<>(word.getLineNos());

        lineNos.add(0);
        word.setLineNos(lineNos);

        validateNotEquivalent(state2);
    }

    @Test
    public void testMissingLineNo() {
        SessionState state2 = buildSession();
        SessionWord word = state2.getOrderedUses().get(0);
        List<Integer> lineNos = word.getLineNos();

        lineNos = lineNos.subList(0, lineNos.size() - 1);
        word.setLineNos(lineNos);

        validateNotEquivalent(state2);
    }

    @Test
    public void testLineNoOrder() {
        SessionState state2 = buildSession();
        SessionWord word = state2.getOrderedUses().get(0);
        List<Integer> lineNos = new ArrayList<>(word.getLineNos());

        Collections.reverse(lineNos);
        word.setLineNos(lineNos);

        validateNotEquivalent(state2);
    }

    private void validateEquivalent(final SessionState state2) {
        assertTrue(state1.isEquivalent(state2));
    }

    private void validateNotEquivalent(final SessionState state2) {
        assertFalse(state1.isEquivalent(state2));
    }
}
