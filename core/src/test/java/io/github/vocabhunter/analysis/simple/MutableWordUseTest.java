/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.simple;

import io.github.vocabhunter.analysis.core.CoreTool;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.model.WordUse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MutableWordUseTest {
    private static final int LINE_10 = 10;

    private static final int LINE_20 = 20;

    private final MutableWordUse target = new MutableWordUse();

    @Test
    public void testToWordUseEmpty() {
        assertThrows(VocabHunterException.class, target::toWordUse);
    }

    @Test
    public void testAccumulateSingleUse() {
        target.accumulate(new AnalysisRecord("Word", LINE_10));

        validate("Word", 1, LINE_10);
    }

    @Test
    public void testAccumulateTwoUsesSameLine() {
        target.accumulate(new AnalysisRecord("Word", LINE_10));
        target.accumulate(new AnalysisRecord("WORD", LINE_10));

        validate("Word", 2, LINE_10);
    }

    @Test
    public void testAccumulateTwoUsesDifferentLines() {
        target.accumulate(new AnalysisRecord("WORD", LINE_20));
        target.accumulate(new AnalysisRecord("Word", LINE_10));

        validate("Word", 2, LINE_10, LINE_20);
    }

    @Test
    public void testCombineBothEmpty() {
        MutableWordUse rhs = new MutableWordUse();

        target.combine(rhs);

        assertThrows(VocabHunterException.class, target::toWordUse);
    }

    @Test
    public void testCombineEmptyLeftHandSide() {
        MutableWordUse rhs = new MutableWordUse();

        rhs.accumulate(new AnalysisRecord("Word", LINE_10));
        target.combine(rhs);

        validate("Word", 1, LINE_10);
    }

    @Test
    public void testCombineEmptyRightHandSide() {
        MutableWordUse rhs = new MutableWordUse();

        target.accumulate(new AnalysisRecord("Word", LINE_10));
        target.combine(rhs);

        validate("Word", 1, LINE_10);
    }

    @Test
    public void testCombineBothSidesUsed() {
        MutableWordUse rhs = new MutableWordUse();

        target.accumulate(new AnalysisRecord("Word", LINE_10));
        rhs.accumulate(new AnalysisRecord("WORD", LINE_20));
        target.combine(rhs);

        validate("Word", 2, LINE_10, LINE_20);
    }

    public void validate(final String word, final int useCount, final Integer... lineNos) {
        WordUse expected = new WordUse(word, useCount, CoreTool.listOf(lineNos));

        assertEquals(expected, target.toWordUse());
    }
}
