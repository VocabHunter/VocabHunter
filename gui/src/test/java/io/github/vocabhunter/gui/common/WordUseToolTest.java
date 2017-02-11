/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import org.junit.Test;

import java.util.List;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class WordUseToolTest {
    @Test
    public void testEmpty() {
        validate("", "");
    }

    @Test
    public void testSingleWord() {
        validate("and", "and");
    }

    @Test
    public void testWordAtStart() {
        validate("And then there were none.", "And", " then there were none.");
    }

    @Test
    public void testWordInMiddle() {
        validate("Chalk and cheese.", "Chalk ", "and", " cheese.");
    }

    @Test
    public void testWordAtEnd() {
        validate("Try ending with and", "Try ending with ", "and");
    }

    @Test
    public void testPrefix() {
        validate("Andian", "Andian");
    }

    @Test
    public void testSuffix() {
        validate("Sand", "Sand");
    }

    @Test
    public void testInfix() {
        validate("Sandy", "Sandy");
    }

    @Test
    public void testVaried() {
        validate("And then the sandy and andian and and", "And", " then the sandy ", "and", " andian ", "and", " ", "and");
    }

    private void validate(final String use, final String... expected) {
        WordUseTool tool = new WordUseTool("and", use);
        List<String> actual = tool.stream()
                .collect(toList());

        assertEquals("Use: " + use, listOf(expected), actual);
    }
}
