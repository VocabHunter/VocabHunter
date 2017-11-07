/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.search;

import io.github.vocabhunter.gui.common.SequencedWord;
import io.github.vocabhunter.gui.common.TestSequencedWord;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatchMakerTest {
    private static final String ACCENTED_MATCH = "\u00F3r";

    private static final SequencedWord WORD = new TestSequencedWord("Word");

    private static final SequencedWord EMPTY = new TestSequencedWord("");

    @Test
    public void testEmptyMatcher() {
        Predicate<SequencedWord> target = SearchTool.matchMaker("");

        assertTrue(target.test(WORD), "Empty matcher");
    }

    @Test
    public void testEmptyWord() {
        Predicate<SequencedWord> target = SearchTool.matchMaker("and");

        assertFalse(target.test(EMPTY), "Empty word");
    }

    @Test
    public void testBothEmpty() {
        Predicate<SequencedWord> target = SearchTool.matchMaker("");

        assertTrue(target.test(EMPTY), "Both empty");
    }

    @Test
    public void testNoMatch() {
        Predicate<SequencedWord> target = SearchTool.matchMaker("x");

        assertFalse(target.test(WORD), "No match");
    }

    @Test
    public void testContainedMatch() {
        Predicate<SequencedWord> target = SearchTool.matchMaker("or");

        assertTrue(target.test(WORD), "Contained match");
    }

    @Test
    public void testExactMatch() {
        Predicate<SequencedWord> target = SearchTool.matchMaker("Word");

        assertTrue(target.test(WORD), "Exact match");
    }

    @Test
    public void testAccentedMatch() {
        Predicate<SequencedWord> target = SearchTool.matchMaker(ACCENTED_MATCH);

        assertTrue(target.test(WORD), "Accented match");
    }

    @Test
    public void testIgnoreCaseMatch() {
        Predicate<SequencedWord> target = SearchTool.matchMaker("WORD");

        assertTrue(target.test(WORD), "Exact match");
    }

    @Test
    public void testRemoveSpaceMatch() {
        Predicate<SequencedWord> target = SearchTool.matchMaker(" or ");

        assertTrue(target.test(WORD), "Remove space");
    }
}
