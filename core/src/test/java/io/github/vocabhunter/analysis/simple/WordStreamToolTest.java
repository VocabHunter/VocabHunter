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
    public void testEmpty() throws Exception {
        validate("");
    }

    @Test
    public void testOneQuote() throws Exception {
        validate("'");
    }

    @Test
    public void testOneDot() throws Exception {
        validate(".");
    }

    @Test
    public void testOneLetter() throws Exception {
        validate("a", "a");
    }

    @Test
    public void testSimple() throws Exception {
        validate("This is a test.", "This", "is", "a", "test");
    }

    @Test
    public void testExtraSpace() throws Exception {
        validate(" This  is  a  test. ", "This", "is", "a", "test");
    }

    @Test
    public void testOuterDoubleQuote() throws Exception {
        validate("\"This is a test.\"", "This", "is", "a", "test");
    }

    @Test
    public void testOuterSingleQuote() throws Exception {
        validate("'This is a test.'", "This", "is", "a", "test");
    }

    @Test
    public void testInnerDoubleQuote() throws Exception {
        validate("This \"is a\" test.", "This", "is", "a", "test");
    }

    @Test
    public void testInnerSingleQuote() throws Exception {
        validate("This 'is a' test.", "This", "is", "a", "test");
    }

    @Test
    public void testCommaSpaceQuote() throws Exception {
        validate("This, 'is a' test.", "This", "is", "a", "test");
    }

    @Test
    public void testDoubleDash() throws Exception {
        validate("This--is a--test.", "This", "is", "a", "test");
    }

    @Test
    public void testDont() throws Exception {
        validate("Don't", "Don't");
    }

    @Test
    public void testDontAtStart() throws Exception {
        validate("Don't wait", "Don't", "wait");
    }

    @Test
    public void testDontAtEnd() throws Exception {
        validate("Wait don't", "Wait", "don't");
    }

    @Test
    public void testDontInMiddle() throws Exception {
        validate("I don't wait", "I", "don't", "wait");
    }

    @Test
    public void testSingleQuoteDont() throws Exception {
        validate("I 'don't' wait", "I", "don't", "wait");
    }

    @Test
    public void testDoubleQuoteDont() throws Exception {
        validate("I \"don't\" wait", "I", "don't", "wait");
    }

    private void validate(final String line, final String... expected) {
        List<String> actual = WordStreamTool.words(line)
            .collect(Collectors.toList());

        assertEquals(listOf(expected), actual, "Words");
    }
}
