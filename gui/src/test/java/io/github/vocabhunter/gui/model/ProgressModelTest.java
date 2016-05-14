/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.marked.WordState;
import javafx.beans.binding.NumberBinding;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals("Marked", 4, target.markedProperty().intValue());
    }

    @Test
    public void testTotal() {
        target.updateProgress(1, 3, 5, 7);
        assertEquals("Total", 16, target.totalProperty().intValue());
    }

    private void initialiseValues() {
        target.updateProgress(10, 10, 10, 10);
    }

    private void validateBasic(final int known, final int unknown, final int unseenUnfiltered, final int unseenFiltered) {
        assertEquals("Known", known, target.knownProperty().intValue());
        assertEquals("Unknown", unknown, target.unknownProperty().intValue());
        assertEquals("Unseen Unfiltered", unseenUnfiltered, target.unseenUnfilteredProperty().intValue());
        assertEquals("Unseen Filtered", unseenFiltered, target.unseenFilteredProperty().intValue());
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
        assertEquals(name, expected, Math.round(property.doubleValue()));
    }
}
