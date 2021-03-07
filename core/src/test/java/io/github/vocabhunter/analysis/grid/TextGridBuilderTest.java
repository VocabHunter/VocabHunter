/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.vocabhunter.test.analysis.grid.GridTestTool.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextGridBuilderTest {

    private final TextGridBuilder target = new TextGridBuilderImpl();

    @Test
    public void testEmpty() {
        assertEquals(emptyGrid(), target.build(List.of()));
    }

    @Test
    public void testUnchanged() {
        assertEquals(grid(), target.build(normalisedGridLines()));
    }

    @Test
    public void testNormalise() {
        assertEquals(grid(), target.build(unnormalisedGridLines()));
    }

    @Test
    public void testLegalSpacing() {
        assertEquals(legalSpacingGrid(), target.build(legalSpacingGridLines()));
    }
}
