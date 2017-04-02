/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.search;

import io.github.vocabhunter.gui.common.SequencedWord;
import io.github.vocabhunter.gui.common.TestSequencedWord;
import org.junit.Test;

import java.util.List;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static io.github.vocabhunter.gui.search.SearchTool.getMatchIndex;
import static io.github.vocabhunter.gui.search.SearchTool.getPreviousMatchIndex;
import static org.junit.Assert.assertEquals;

public class MatchIndexTest {
    private final SequencedWord one = new TestSequencedWord("one", 1);

    private final SequencedWord two = new TestSequencedWord("two", 2);

    private final SequencedWord three = new TestSequencedWord("three", 3);

    private final SequencedWord four = new TestSequencedWord("four", 4);

    private final SequencedWord five = new TestSequencedWord("five", 5);

    private final List<SequencedWord> emptyMatches = listOf();

    private final List<SequencedWord> matches = listOf(two, four);

    @Test
    public void testEmpty() {
        validate(emptyMatches, one, -1, -1);
    }

    @Test
    public void testWordOne() {
        validate(one, -1, -1);
    }

    @Test
    public void testWordTwo() {
        validate(two, 0, -1);
    }

    @Test
    public void testWordThree() {
        validate(three, -1, 0);
    }

    @Test
    public void testWordFour() {
        validate(four, 1, 0);
    }

    @Test
    public void testWordFive() {
        validate(five, -1, 1);
    }

    private void validate(final SequencedWord word, final int expectedIndex, final int expectedPreviousIndex) {
        validate(matches, word, expectedPreviousIndex, expectedIndex);
    }

    private void validate(final List<SequencedWord> matchList, final SequencedWord word, final int expectedPreviousIndex, final int expectedIndex) {
        assertEquals("Match index", expectedIndex, getMatchIndex(matchList, word));
        assertEquals("Previous match index", expectedPreviousIndex, getPreviousMatchIndex(matchList, word));
    }
}
