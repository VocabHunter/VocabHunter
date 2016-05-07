/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.marked.WordState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProgressModelTest {
    private final ProgressModel target = new ProgressModel();

    @Test
    public void testInitial() {
        validate(0, 0, 0, 0);
    }

    @Test
    public void testUpdateProgress() {
        target.updateProgress(1, 2, 3, 4);
        validate(1, 2, 3, 4);
    }

    @Test
    public void testToKnown() {
        initialiseValues();
        target.updateWord(WordState.UNKNOWN, WordState.KNOWN);
        validate(11, 9, 10, 10);
    }

    @Test
    public void testToUnseen() {
        initialiseValues();
        target.updateWord(WordState.KNOWN, WordState.UNSEEN);
        validate(9, 10, 11, 10);
    }

    @Test
    public void testToUnknown() {
        initialiseValues();
        target.updateWord(WordState.UNSEEN, WordState.UNKNOWN);
        validate(10, 11, 9, 10);
    }

    private void initialiseValues() {
        target.updateProgress(10, 10, 10, 10);
    }

    private void validate(final int known, final int unknown, final int unseenUnfiltered, final int unseenFiltered) {
        assertEquals("Known", known, target.knownProperty().intValue());
        assertEquals("Unknown", unknown, target.unknownProperty().intValue());
        assertEquals("Unseen Unfiltered", unseenUnfiltered, target.unseenUnfilteredProperty().intValue());
        assertEquals("Unseen Filtered", unseenFiltered, target.unseenFilteredProperty().intValue());
    }
}
