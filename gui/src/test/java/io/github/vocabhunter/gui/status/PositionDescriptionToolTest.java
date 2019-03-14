/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import io.github.vocabhunter.gui.model.PositionModel;
import io.github.vocabhunter.gui.model.ProgressModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionDescriptionToolTest {
    private final PositionModel position = new PositionModel();

    private final ProgressModel progress = new ProgressModel();

    private final PositionDescriptionTool target = new PositionDescriptionTool();

    @Test
    public void testAnalysisModeOff() {
        String result = target.describe(position, progress);

        assertEquals("", result);
    }

    @Test
    public void testEditSingleWord() {
        position.setAnalysisMode(true);
        position.setEditable(true);
        position.setPositionIndex(0);
        position.setSize(1);

        String result = target.describe(position, progress);

        assertEquals("Word 1 of 1 word", result);
    }

    @Test
    public void testEditTwentyWords() {
        position.setAnalysisMode(true);
        position.setEditable(true);
        position.setPositionIndex(9);
        position.setSize(20);

        String result = target.describe(position, progress);

        assertEquals("Word 10 of 20 words", result);
    }

    @Test
    public void testFilterSingleWord() {
        position.setAnalysisMode(true);
        position.setEditable(true);
        position.setPositionIndex(9);
        position.setSize(20);
        progress.updateProgress(0, 0, 0, 1);

        String result = target.describe(position, progress);

        assertEquals("Word 10 of 20 words (1 word hidden by filter)", result);
    }

    @Test
    public void testFilterFiveWords() {
        position.setAnalysisMode(true);
        position.setEditable(true);
        position.setPositionIndex(9);
        position.setSize(20);
        progress.updateProgress(0, 0, 0, 5);

        String result = target.describe(position, progress);

        assertEquals("Word 10 of 20 words (5 words hidden by filter)", result);
    }

    @Test
    public void testThreeUnknownWords() {
        position.setAnalysisMode(true);
        position.setEditable(false);
        position.setPositionIndex(2);
        position.setSize(5);

        String result = target.describe(position, progress);

        assertEquals("Word 3 of 5 words marked as unknown", result);
    }
}
