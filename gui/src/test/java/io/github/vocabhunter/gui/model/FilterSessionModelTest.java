/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.marked.TestMarkedWord;
import io.github.vocabhunter.analysis.marked.WordState;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class FilterSessionModelTest {
    @Test
    public void testEmpty() {
        FilterSessionModel target = build(0, 0);

        validate(target, 0, 0, false, true, FilterSessionModel.ERROR);
    }

    @Test
    public void testSimple() {
        FilterSessionModel target = build(1000, 100);

        validate(target, 1000, 1100, false, false, "1,000");
    }

    @Test
    public void testChangeState() {
        FilterSessionModel target = build(1000, 100);

        target.setIncludeUnknown(true);

        validate(target, 1000, 1100, true, false, "1,100");
    }

    @Test
    public void testNoneKnown() {
        FilterSessionModel target = build(0, 100);

        validate(target, 0, 100, false, true, FilterSessionModel.ERROR);
    }

    @Test
    public void testOnlyUnknown() {
        FilterSessionModel target = build(0, 100);

        target.setIncludeUnknown(true);

        validate(target, 0, 100, true, false, "100");
    }

    private void validate(final FilterSessionModel target, final int knownCount, final int seenCount, final boolean isIncludeUnknown, final boolean isError, final String countDescription) {
        assertEquals("Known count", knownCount, target.getKnownCount());
        assertEquals("Seen count", seenCount, target.getSeenCount());
        assertEquals("Include unknown", isIncludeUnknown, target.isIncludeUnknown());
        assertEquals("Error state", isError, target.isError());
        assertEquals("Count description", countDescription, target.getCountDescription());
    }

    private FilterSessionModel build(final int knownCount, final int unknownCount) {
        List<MarkedWord> words = IntStream.range(0, knownCount + unknownCount)
            .mapToObj(i -> new TestMarkedWord("Word " + i, 1, i < knownCount ? WordState.KNOWN : WordState.UNKNOWN))
            .collect(Collectors.toList());

        return new FilterSessionModel(words);
    }
}
