/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.i18n.I18nManagerImpl;
import io.github.vocabhunter.gui.model.PositionModel;
import io.github.vocabhunter.gui.model.ProgressModel;
import javafx.beans.value.ObservableStringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionDescriptionToolTest {
    private final PositionModel position = new PositionModel();

    private final ProgressModel progress = new ProgressModel();

    private final I18nManager i18nManager = I18nManagerImpl.createForDefaultLocale();

    private final PositionDescriptionTool target = new PositionDescriptionTool(i18nManager);

    @Test
    public void testAnalysisModeOff() {
        validate("");
    }

    @Test
    public void testEditSingleWord() {
        position.setAnalysisMode(true);
        position.setEditable(true);
        position.setPositionIndex(0);
        position.setSize(1);

        validate("Word 1 of 1 word");
    }

    @Test
    public void testEditTwentyWords() {
        position.setAnalysisMode(true);
        position.setEditable(true);
        position.setPositionIndex(9);
        position.setSize(20);

        validate("Word 10 of 20 words");
    }

    @Test
    public void testFilterSingleWord() {
        position.setAnalysisMode(true);
        position.setEditable(true);
        position.setPositionIndex(9);
        position.setSize(20);
        progress.updateProgress(0, 0, 0, 1);

        validate("Word 10 of 20 words (1 word hidden by filter)");
    }

    @Test
    public void testFilterFiveWords() {
        position.setAnalysisMode(true);
        position.setEditable(true);
        position.setPositionIndex(9);
        position.setSize(20);
        progress.updateProgress(0, 0, 0, 5);

        validate("Word 10 of 20 words (5 words hidden by filter)");
    }

    @Test
    public void testThreeUnknownWords() {
        position.setAnalysisMode(true);
        position.setEditable(false);
        position.setPositionIndex(2);
        position.setSize(5);

        validate("Word 3 of 5 words marked as unknown");
    }

    private void validate(final String s) {
        ObservableStringValue result = target.createBinding(position, progress);

        assertEquals(s, result.getValue());
    }
}
