/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.marked.WordState;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.vocabhunter.analysis.marked.WordState.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InitialSelectionToolTest {
    @Test
    public void testNull() {
        assertThrows(VocabHunterException.class, () -> InitialSelectionTool.nextWord(null));
    }

    @Test
    public void testEmpty() {
        assertThrows(VocabHunterException.class, () -> InitialSelectionTool.nextWord(words()));
    }

    @Test
    public void testSingleUnseen() {
        validate(0, UNSEEN);
    }

    @Test
    public void testSingleKnown() {
        validate(0, KNOWN);
    }

    @Test
    public void testInitial() {
        validate(0, UNSEEN, UNSEEN, UNSEEN, UNSEEN, UNSEEN, UNSEEN);
    }

    @Test
    public void testUnfinishedList() {
        validate(5, UNSEEN, KNOWN, UNKNOWN, UNSEEN, KNOWN, UNSEEN, UNSEEN, UNSEEN);
    }

    @Test
    public void testFinishedList() {
        validate(7, KNOWN, KNOWN, UNKNOWN, KNOWN, KNOWN, UNKNOWN, KNOWN, KNOWN);
    }

    @Test
    public void testFinishedListWithGaps() {
        validate(7, KNOWN, KNOWN, UNKNOWN, UNSEEN, KNOWN, UNKNOWN, KNOWN, KNOWN);
    }

    private void validate(final int expectedIndex, final WordState... states) {
        List<WordModel> words = words(states);
        WordModel result = InitialSelectionTool.nextWord(words);

        assertEquals(expectedIndex, result.getSequenceNo(), "Selected word index");
    }

    private List<WordModel> words(final WordState... states) {
        List<WordModel> result = new ArrayList<>(states.length);
        int index = 0;

        for (WordState state : states) {
            WordModel model = new WordModel(index, "word " + index, Collections.emptyList(), 0, state, null);
            result.add(model);
            ++index;
        }

        return result;
    }
}
