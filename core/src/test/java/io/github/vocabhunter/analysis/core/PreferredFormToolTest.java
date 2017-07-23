/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import org.junit.Test;

import static io.github.vocabhunter.analysis.core.PreferredFormTool.preferredForm;
import static junit.framework.TestCase.assertEquals;

public class PreferredFormToolTest {
    @Test
    public void testIdenticalUpper() {
        validate("Word", "Word", "Word");
    }

    @Test
    public void testIdenticalLower() {
        validate("word", "word", "word");
    }

    @Test
    public void testIdenticalAllCaps() {
        validate("WORD", "WORD", "WORD");
    }

    @Test
    public void testMixedCapsLeft() {
        validate("Word", "WORD", "Word");
    }

    @Test
    public void testMixedCapsRight() {
        validate("WORD", "Word", "Word");
    }

    @Test
    public void testLowerLeft() {
        validate("word", "Word", "word");
    }

    @Test
    public void testLowerRight() {
        validate("Word", "word", "word");
    }

    @Test
    public void testIncompatible() {
        validate("wORd", "WorD", "word");
    }

    private void validate(final String w1, final String w2, final String expected) {
        String actual = preferredForm(w1, w2);

        assertEquals("Preferred", expected, actual);
    }
}
