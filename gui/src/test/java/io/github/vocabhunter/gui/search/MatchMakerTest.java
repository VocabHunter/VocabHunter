/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.search;

import io.github.vocabhunter.gui.common.SequencedWord;
import io.github.vocabhunter.gui.common.TestSequencedWord;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MatchMakerTest {
    private static final String ACCENTED_MATCH = "\u00F3r";

    private static final SequencedWord WORD = new TestSequencedWord("Word");

    private static final SequencedWord EMPTY = new TestSequencedWord("");

    @Test
    public void testEmptyMatcher() {
        Predicate<SequencedWord> target = SearchTool.matchMaker("");

        assertTrue("Empty matcher", target.test(WORD));
    }

    @Test
    public void testEmptyWord() {
        Predicate<SequencedWord> target = SearchTool.matchMaker("and");

        assertFalse("Empty word", target.test(EMPTY));
    }

    @Test
    public void testBothEmpty() {
        Predicate<SequencedWord> target = SearchTool.matchMaker("");

        assertTrue("Both empty", target.test(EMPTY));
    }

    @Test
    public void testNoMatch() {
        Predicate<SequencedWord> target = SearchTool.matchMaker("x");

        assertFalse("No match", target.test(WORD));
    }

    @Test
    public void testContainedMatch() {
        Predicate<SequencedWord> target = SearchTool.matchMaker("or");

        assertTrue("Contained match", target.test(WORD));
    }

    @Test
    public void testExactMatch() {
        Predicate<SequencedWord> target = SearchTool.matchMaker("Word");

        assertTrue("Exact match", target.test(WORD));
    }

    @Test
    public void testAccentedMatch() {
        Predicate<SequencedWord> target = SearchTool.matchMaker(ACCENTED_MATCH);

        assertTrue("Accented match", target.test(WORD));
    }

    @Test
    public void testIgnoreCaseMatch() {
        Predicate<SequencedWord> target = SearchTool.matchMaker("WORD");

        assertTrue("Exact match", target.test(WORD));
    }

    @Test
    public void testRemoveSpaceMatch() {
        Predicate<SequencedWord> target = SearchTool.matchMaker(" or ");

        assertTrue("Remove space", target.test(WORD));
    }
}
