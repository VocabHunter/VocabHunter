/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.marked;

import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.analysis.model.AnalysisWord;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static io.github.vocabhunter.analysis.marked.WordState.*;
import static java.util.Collections.emptyList;
import static junit.framework.TestCase.*;

public class MarkToolTest {
    private final List<MarkedWord> allWords = listOf(
        word(KNOWN, 1),
        word(KNOWN, 2),
        word(KNOWN, 3),
        word(UNKNOWN, 1),
        word(UNKNOWN, 2),
        word(UNKNOWN, 3),
        word(UNKNOWN, 4),
        word(UNKNOWN, 5),
        word(UNSEEN, 1),
        word(UNSEEN, 2),
        word(UNSEEN, 3),
        word(UNSEEN, 4),
        word(UNSEEN, 5),
        word(UNSEEN, 6),
        word(UNSEEN, 7)
    );

    private final List<MarkedWord> allUnfiltered = allWords.subList(0, allWords.size() - 1);

    private final Set<AnalysisWord> filterSet = new HashSet<>(listOf(
        word(KNOWN, 3),
        word(UNKNOWN, 5),
        word(UNSEEN, 7)
    ));

    private final WordFilter filter = w -> !filterSet.contains(w);

    private final MarkTool<MarkedWord> emptyTarget = new MarkTool<>(filter, emptyList());

    private final MarkTool<MarkedWord> target = new MarkTool<>(filter, allWords);

    @Test
    public void testEmptyIsValidFilter() {
        assertFalse(emptyTarget.isValidFilter());
    }

    @Test
    public void testEmptyGetShownWords() {
        assertTrue(emptyTarget.getShownWords().isEmpty());
    }

    @Test
    public void testEmptyGetKnown() {
        assertEquals(0, emptyTarget.getKnown());
    }

    @Test
    public void testEmptyGetUnknown() {
        assertEquals(0, emptyTarget.getUnknown());
    }

    @Test
    public void testEmptyGetUnseenUnfiltered() {
        assertEquals(0, emptyTarget.getUnseenUnfiltered());
    }

    @Test
    public void testEmptyGetUnseenFiltered() {
        assertEquals(0, emptyTarget.getUnseenFiltered());
    }

    @Test
    public void testIsValidFilter() {
        assertTrue(target.isValidFilter());
    }

    @Test
    public void testGetShownWords() {
        assertEquals(allUnfiltered, target.getShownWords());
    }

    @Test
    public void testGetKnown() {
        assertEquals(3, target.getKnown());
    }

    @Test
    public void testGetUnknown() {
        assertEquals(5, target.getUnknown());
    }

    @Test
    public void testGetUnseenUnfiltered() {
        assertEquals(6, target.getUnseenUnfiltered());
    }

    @Test
    public void testGetUnseenFiltered() {
        assertEquals(1, target.getUnseenFiltered());
    }

    private MarkedWord word(final WordState state, final int number) {
        String word = String.format("%s %s", state, number);

        return new TestMarkedWord(word, number, state);
    }
}
