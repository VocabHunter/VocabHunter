/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.search;

import io.github.vocabhunter.analysis.core.CoreTool;
import io.github.vocabhunter.gui.common.TestSequencedWord;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearcherTest {
    private static final TestSequencedWord WORD_AA = new TestSequencedWord("AA", 10);

    private static final TestSequencedWord WORD_AB = new TestSequencedWord("AB", 20);

    private static final TestSequencedWord WORD_AC = new TestSequencedWord("AC", 30);

    private static final TestSequencedWord WORD_BC = new TestSequencedWord("BC", 40);

    private static final List<TestSequencedWord> EMPTY_WORDS = Collections.emptyList();

    private static final List<TestSequencedWord> WORDS = CoreTool.listOf(WORD_AA, WORD_AB, WORD_AC, WORD_BC);

    private final Searcher<TestSequencedWord> target = new Searcher<>(SearchTool::matchMaker);

    @Test
    public void testEmptyList() {
        SearchResult<TestSequencedWord> result = target.buildResult(EMPTY_WORDS, WORD_AA, "AA");

        validateNoMatch(result);
    }

    @Test
    public void testEmptySearchText() {
        SearchResult<TestSequencedWord> result = target.buildResult(WORDS, WORD_AA, "");

        validateEmptySearch(result);
    }

    @Test
    public void testNoMatch() {
        SearchResult<TestSequencedWord> result = target.buildResult(WORDS, WORD_AA, "no-match");

        validateNoMatch(result);
    }

    @Test
    public void testMatchFirstWord() {
        SearchResult<TestSequencedWord> result = target.buildResult(WORDS, WORD_AA, "A");

        validateMatch(result, "1 of 3 matches", null, WORD_AB, WORD_AB);
    }

    @Test
    public void testMatchSecondWord() {
        SearchResult<TestSequencedWord> result = target.buildResult(WORDS, WORD_AB, "A");

        validateMatch(result, "2 of 3 matches", WORD_AA, WORD_AC, WORD_AC);
    }

    @Test
    public void testMatchThirdWord() {
        SearchResult<TestSequencedWord> result = target.buildResult(WORDS, WORD_AC, "A");

        validateMatch(result, "3 of 3 matches", WORD_AB, null, WORD_AA);
    }

    @Test
    public void testBeforeFirstWord() {
        SearchResult<TestSequencedWord> result = target.buildResult(WORDS, WORD_AA, "B");

        validateMatch(result, "2 matches", null, WORD_AB, WORD_AB);
    }

    @Test
    public void testBetweenMatches() {
        SearchResult<TestSequencedWord> result = target.buildResult(WORDS, WORD_AC, "B");

        validateMatch(result, "2 matches", WORD_AB, WORD_BC, WORD_BC);
    }

    @Test
    public void testAfterMatches() {
        SearchResult<TestSequencedWord> result = target.buildResult(WORDS, WORD_BC, "A");

        validateMatch(result, "3 matches", WORD_AC, null, WORD_AA);
    }

    private void validateEmptySearch(final SearchResult<TestSequencedWord> result) {
        validate(result, "", null, null, null, false);
    }

    private void validateNoMatch(final SearchResult<TestSequencedWord> result) {
        validate(result, "No matches", null, null, null, true);
    }

    private void validateMatch(
        final SearchResult<TestSequencedWord> result, final String matchDescription,
        final TestSequencedWord previousMatch, final TestSequencedWord nextMatch, final TestSequencedWord wrapMatch) {
        validate(result, matchDescription, previousMatch, nextMatch, wrapMatch, false);
    }

    private void validate(
        final SearchResult<TestSequencedWord> result, final String matchDescription,
        final TestSequencedWord previousMatch, final TestSequencedWord nextMatch, final TestSequencedWord wrapMatch, final boolean isSearchFail) {
        SearchResult<TestSequencedWord> expected = new SearchResult<>(matchDescription, previousMatch, nextMatch, wrapMatch, isSearchFail);

        assertEquals(expected, result);
    }
}
