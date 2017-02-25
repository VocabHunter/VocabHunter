/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static io.github.vocabhunter.analysis.grid.GridTestTool.acceptedCell;
import static io.github.vocabhunter.analysis.grid.GridTestTool.rejectedCell;
import static org.junit.Assert.assertEquals;

public class GridWordsExtractorTest {
    private static final String WORD_1 = "WORD1";

    private static final String WORD_2 = "WORD2";

    private static final String WORD_3 = "WORD3";

    private static final GridLine LINE_EMPTY = new GridLine();

    private static final GridLine LINE_ACCEPTED_WORD = new GridLine(acceptedCell(WORD_1));

    private static final GridLine LINE_REJECTED_WORD = new GridLine(rejectedCell(WORD_1));

    private static final GridLine LINE_TWO_COLUMNS = new GridLine(acceptedCell(WORD_2), acceptedCell(WORD_3));

    private static final List<GridLine> GRID_1 = listOf(LINE_ACCEPTED_WORD, LINE_TWO_COLUMNS);

    private final GridWordsExtractor target = new GridWordsExtractorImpl();

    @Test
    public void testEmpty() {
        validate(listOf(), columns());
    }

    @Test
    public void testNoLines() {
        validate(listOf(), columns(0));
    }

    @Test
    public void testEmptyLine() {
        validate(listOf(LINE_EMPTY), columns(0));
    }

    @Test
    public void testEmptyColumns() {
        validate(listOf(LINE_ACCEPTED_WORD), columns());
    }

    @Test
    public void testAcceptedWord() {
        validate(listOf(LINE_ACCEPTED_WORD), columns(0), WORD_1);
    }

    @Test
    public void testRejectedWord() {
        validate(listOf(LINE_REJECTED_WORD), columns(0));
    }

    @Test
    public void testFirstColumn() {
        validate(GRID_1, columns(0), WORD_1, WORD_2);
    }

    @Test
    public void testSecondColumn() {
        validate(GRID_1, columns(1), WORD_3);
    }

    @Test
    public void testBothColumns() {
        validate(GRID_1, columns(0, 1), WORD_1, WORD_2, WORD_3);
    }

    private void validate(final List<GridLine> lines, final Set<Integer> columns, final String... words) {
        Set<String> result = new TreeSet<>(target.words(lines, columns));
        Set<String> expected = new TreeSet<>(listOf(words));

        assertEquals(expected, result);
    }

    private Set<Integer> columns(final Integer... columns) {
        return new TreeSet<>(listOf(columns));
    }
}
