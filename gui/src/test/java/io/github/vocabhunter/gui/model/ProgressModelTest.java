/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.marked.WordState;
import javafx.beans.binding.NumberBinding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgressModelTest {
    private final ProgressModel target = new ProgressModel();

    @Test
    public void testInitial() {
        validateBasic(0, 0, 0, 0);
    }

    @Test
    public void testInitialPercent() {
        validatePercent(0, 0, 0, 0, 0, 0);
    }

    @Test
    public void testUpdateProgress() {
        target.updateProgress(1, 2, 3, 4);
        validateBasic(1, 2, 3, 4);
    }

    @Test
    public void testUpdateProgressPercent() {
        target.updateProgress(1, 3, 5, 7);
        validatePercent(6, 19, 31, 44, 44, 56);
    }

    @Test
    public void testToKnown() {
        initialiseValues();
        target.updateWord(WordState.UNKNOWN, WordState.KNOWN);
        validateBasic(11, 9, 10, 10);
    }

    @Test
    public void testToUnseen() {
        initialiseValues();
        target.updateWord(WordState.KNOWN, WordState.UNSEEN);
        validateBasic(9, 10, 11, 10);
    }

    @Test
    public void testToUnknown() {
        initialiseValues();
        target.updateWord(WordState.UNSEEN, WordState.UNKNOWN);
        validateBasic(10, 11, 9, 10);
    }

    @Test
    public void testMarked() {
        target.updateProgress(1, 3, 5, 7);
        assertEquals(4, target.markedProperty().intValue(), "Marked");
    }

    @Test
    public void testTotal() {
        target.updateProgress(1, 3, 5, 7);
        assertEquals(16, target.totalProperty().intValue(), "Total");
    }

    private void initialiseValues() {
        target.updateProgress(10, 10, 10, 10);
    }

    private void validateBasic(final int known, final int unknown, final int unseenUnfiltered, final int unseenFiltered) {
        assertEquals(known, target.knownProperty().intValue(), "Known");
        assertEquals(unknown, target.unknownProperty().intValue(), "Unknown");
        assertEquals(unseenUnfiltered, target.unseenUnfilteredProperty().intValue(), "Unseen Unfiltered");
        assertEquals(unseenFiltered, target.unseenFilteredProperty().intValue(), "Unseen Filtered");
    }

    private void validatePercent(final int known, final int unknown, final int unseenUnfiltered, final int unseenFiltered, final int marked, final int remaining) {
        validatePercentage("Known Percent", known, target.knownPercentProperty());
        validatePercentage("Unknown Percent", unknown, target.unknownPercentProperty());
        validatePercentage("Unseen Unfiltered Percent", unseenUnfiltered, target.unseenUnfilteredPercentProperty());
        validatePercentage("Unseen Filtered Percent", unseenFiltered, target.unseenFilteredPercentProperty());

        validatePercentage("Marked", marked, target.markedPercentVisibleProperty());
        validatePercentage("Remaining", remaining, target.unseenUnfilteredPercentVisibleProperty());
    }

    private void validatePercentage(final String name, final int expected, final NumberBinding property) {
        assertEquals(expected, Math.round(property.doubleValue()), name);
    }
}
