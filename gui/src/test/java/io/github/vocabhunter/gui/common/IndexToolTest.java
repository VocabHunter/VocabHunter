/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import org.junit.jupiter.api.Test;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IndexToolTest {
    private static final int SIZE = 10;

    private static final int REQUESTED = 5;

    private static final int ONE_BELOW = REQUESTED - 1;

    private static final int TWO_BELOW = REQUESTED - 2;

    private static final int THREE_BELOW = REQUESTED - 3;

    private static final int ONE_ABOVE = REQUESTED + 1;

    private static final int TWO_ABOVE = REQUESTED + 2;

    private static final int FIRST = 0;

    private static final int LAST = SIZE - 1;

    private static final int BEFORE_FIRST = FIRST - 1;

    private static final int AFTER_LAST = LAST + 1;

    @Test
    public void testNoMatch() {
        assertThrows(VocabHunterException.class, this::findClosest);
    }

    @Test
    public void testSimpleMatch() {
        validate(REQUESTED, REQUESTED);
    }

    @Test
    public void testOneBelow() {
        validate(ONE_BELOW, ONE_BELOW, ONE_ABOVE);
    }

    @Test
    public void testOneAbove() {
        validate(ONE_ABOVE, TWO_BELOW, ONE_ABOVE);
    }

    @Test
    public void testTwoBelow() {
        validate(TWO_BELOW, TWO_BELOW, TWO_ABOVE);
    }

    @Test
    public void testTwoAbove() {
        validate(TWO_ABOVE, THREE_BELOW, TWO_ABOVE);
    }

    @Test
    public void testFirst() {
        validate(FIRST, FIRST);
    }

    @Test
    public void testLast() {
        validate(LAST, LAST);
    }

    @Test
    public void testBeforeFirst() {
        assertThrows(VocabHunterException.class, () -> findClosest(BEFORE_FIRST));
    }

    @Test
    public void testAfterLast() {
        assertThrows(VocabHunterException.class, () -> findClosest(AFTER_LAST));
    }

    private void validate(final int expected, final Integer... accepted) {
        int result = findClosest(accepted);

        assertEquals(expected, result);
    }

    private int findClosest(final Integer... accepted) {
        return IndexTool.findClosest(REQUESTED, SIZE, listOf(accepted)::contains);
    }
}
