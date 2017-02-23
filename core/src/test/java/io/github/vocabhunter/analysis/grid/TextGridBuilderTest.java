/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class TextGridBuilderTest {
    private static final TextGrid EMPTY = GridTestTool.emptyGrid();

    private static final TextGrid GRID = GridTestTool.grid();

    private final TextGridBuilder target = new TextGridBuilderImpl();

    @Test
    public void testEmpty() {
        assertEquals(EMPTY, target.build(Collections.emptyList()));
    }

    @Test
    public void testUnchanged() {
        assertEquals(GRID, target.build(GridTestTool.normalisedLines()));
    }

    @Test
    public void testNormalise() {
        assertEquals(GRID, target.build(GridTestTool.unnormalisedLines()));
    }
}
