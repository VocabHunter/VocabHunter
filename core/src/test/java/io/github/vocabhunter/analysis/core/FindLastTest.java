/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import org.junit.Test;

import java.util.Arrays;
import java.util.OptionalInt;

import static org.junit.Assert.assertEquals;

public class FindLastTest {
    private enum Word {
        MATCH, NO_MATCH
    }

    @Test
    public void testEmpty() {
        validateNoMatch();
    }

    @Test
    public void testSingleMatch() {
        validateMatch(0, Word.MATCH);
    }

    @Test
    public void testSingleNoMatch() {
        validateNoMatch(Word.NO_MATCH);
    }

    @Test
    public void testAllMatch() {
        validateMatch(2, Word.MATCH, Word.MATCH, Word.MATCH);
    }

    @Test
    public void testMixed() {
        validateMatch(1, Word.NO_MATCH, Word.MATCH, Word.NO_MATCH, Word.NO_MATCH);
    }

    private void validateMatch(final int expected, final Word... words) {
        assertEquals(OptionalInt.of(expected), result(words));
    }

    private void validateNoMatch(final Word... words) {
        assertEquals(OptionalInt.empty(), result(words));
    }

    private OptionalInt result(final Word... words) {
        return CoreTool.findLast(Arrays.asList(words), w -> w == Word.MATCH);
    }
}
