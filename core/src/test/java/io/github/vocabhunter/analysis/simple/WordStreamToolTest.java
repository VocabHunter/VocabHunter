/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.simple;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordStreamToolTest {
    @Test
    public void testEmpty() {
        validate("");
    }

    @Test
    public void testOneQuote() {
        validate("'");
    }

    @Test
    public void testOneDot() {
        validate(".");
    }

    @Test
    public void testOneLetter() {
        validate("a", "a");
    }

    @Test
    public void testSimple() {
        validate("This is a test.", "This", "is", "a", "test");
    }

    @Test
    public void testExtraSpace() {
        validate(" This  is  a  test. ", "This", "is", "a", "test");
    }

    @Test
    public void testOuterDoubleQuote() {
        validate("\"This is a test.\"", "This", "is", "a", "test");
    }

    @Test
    public void testOuterSingleQuote() {
        validate("'This is a test.'", "This", "is", "a", "test");
    }

    @Test
    public void testInnerDoubleQuote() {
        validate("This \"is a\" test.", "This", "is", "a", "test");
    }

    @Test
    public void testInnerSingleQuote() {
        validate("This 'is a' test.", "This", "is", "a", "test");
    }

    @Test
    public void testCommaSpaceQuote() {
        validate("This, 'is a' test.", "This", "is", "a", "test");
    }

    @Test
    public void testDoubleDash() {
        validate("This--is a--test.", "This", "is", "a", "test");
    }

    @Test
    public void testDont() {
        validate("Don't", "Don't");
    }

    @Test
    public void testDontAtStart() {
        validate("Don't wait", "Don't", "wait");
    }

    @Test
    public void testDontAtEnd() {
        validate("Wait don't", "Wait", "don't");
    }

    @Test
    public void testDontInMiddle() {
        validate("I don't wait", "I", "don't", "wait");
    }

    @Test
    public void testSingleQuoteDont() {
        validate("I 'don't' wait", "I", "don't", "wait");
    }

    @Test
    public void testDoubleQuoteDont() {
        validate("I \"don't\" wait", "I", "don't", "wait");
    }

    private void validate(final String line, final String... expected) {
        List<String> actual = WordStreamTool.words(line)
            .collect(Collectors.toList());

        assertEquals(listOf(expected), actual, "Words");
    }
}
