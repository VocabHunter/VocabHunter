/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextGridManagerFilterTest {
    @Test
    public void testEmpty() {
        validate("", false);
    }

    @Test
    public void testBlank() {
        validate(" ", false);
    }

    @Test
    public void testWord() {
        validate("Hello", false);
    }

    @Test
    public void testApostrophe() {
        validate("Don't", false);
    }

    @Test
    public void testTwoWords() {
        validate("Two Words", true);
    }

    private void validate(final String text, final boolean expected) {
        assertEquals(expected, TextGridManagerImpl.FILTER.test(text));
    }
}
