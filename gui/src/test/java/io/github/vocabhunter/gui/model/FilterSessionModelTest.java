/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.marked.TestMarkedWord;
import io.github.vocabhunter.analysis.marked.WordState;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class FilterSessionModelTest {
    private static final String FILENAME_1 = "file1.wordy";

    private static final String FILENAME_2 = "file2.wordy";

    private static final Path FILE_1 = Paths.get("directory", FILENAME_1);

    private static final Path FILE_2 = Paths.get("directory", FILENAME_2);

    @Test
    public void testEmpty() {
        FilterSessionModel target = build(0, 0);

        validateError(target, 0, 0, false);
    }

    @Test
    public void testSimple() {
        FilterSessionModel target = build(1000, 100);

        validateOk(target, 1000, 1100, false, "1,000");
    }

    @Test
    public void testChangeState() {
        FilterSessionModel target = build(1000, 100);

        target.setIncludeUnknown(true);

        validateOk(target, 1000, 1100, true, "1,100");
    }

    @Test
    public void testNoneKnown() {
        FilterSessionModel target = build(0, 100);

        validateError(target, 0, 100, false);
    }

    @Test
    public void testOnlyUnknown() {
        FilterSessionModel target = build(0, 100);

        target.setIncludeUnknown(true);

        validateOk(target, 0, 100, true, "100");
    }

    @Test
    public void testReplaceSimple() {
        FilterSessionModel target = build(0, 0);

        target.replaceContent(FILE_2, words(1000, 100));

        validateReplaceOk(target, 1000, 1100, false, "1,000");
    }

    @Test
    public void testReplaceError() {
        FilterSessionModel target = build(1000, 100);

        target.replaceContent(FILE_2, words(0, 0));

        validateReplaceError(target, 0, 0, false);
    }

    @Test
    public void testReplaceChangeState() {
        FilterSessionModel target = build(10, 10);

        target.setIncludeUnknown(true);
        target.replaceContent(FILE_2, words(1000, 100));

        validateReplaceOk(target, 1000, 1100, true, "1,100");
    }

    private void validateOk(final FilterSessionModel target, final int knownCount, final int seenCount, final boolean isIncludeUnknown, final String countDescription) {
        validate(target, FILE_1, FILENAME_1, isIncludeUnknown, countDescription, false, knownCount, seenCount);
    }

    private void validateError(final FilterSessionModel target, final int knownCount, final int seenCount, final boolean isIncludeUnknown) {
        validate(target, FILE_1, FILENAME_1, isIncludeUnknown, FilterSessionModel.ERROR, true, knownCount, seenCount);
    }

    private void validateReplaceOk(final FilterSessionModel target, final int knownCount, final int seenCount, final boolean isIncludeUnknown, final String countDescription) {
        validate(target, FILE_2, FILENAME_2, isIncludeUnknown, countDescription, false, knownCount, seenCount);
    }

    private void validateReplaceError(final FilterSessionModel target, final int knownCount, final int seenCount, final boolean isIncludeUnknown) {
        validate(target, FILE_2, FILENAME_2, isIncludeUnknown, FilterSessionModel.ERROR, true, knownCount, seenCount);
    }

    private void validate(final FilterSessionModel target, final Path file, final String filename, final boolean isIncludeUnknown,
                          final String countDescription, final boolean isError, final int knownCount, final int seenCount) {
        assertEquals("File", file, target.getFile());
        assertEquals("Filename", filename, target.getFilename());
        assertEquals("Known count", knownCount, target.getKnownCount());
        assertEquals("Seen count", seenCount, target.getSeenCount());
        assertEquals("Include unknown", isIncludeUnknown, target.isIncludeUnknown());
        assertEquals("Error state", isError, target.isError());
        assertEquals("Count description", countDescription, target.getCountDescription());
        assertEquals("Words", filterSessionWords(knownCount, seenCount - knownCount), target.getSeenWords());
    }

    private FilterSessionModel build(final int knownCount, final int unknownCount) {
        List<MarkedWord> words = words(knownCount, unknownCount);

        return new FilterSessionModel(FILE_1, words);
    }

    private List<FilterSessionWord> filterSessionWords(final int knownCount, final int unknownCount) {
        return words(knownCount, unknownCount).stream()
            .map(FilterSessionWord::new)
            .collect(toList());
    }

    private List<MarkedWord> words(final int knownCount, final int unknownCount) {
        return IntStream.range(0, knownCount + unknownCount)
                .mapToObj(i -> new TestMarkedWord("Word " + i, 1, i < knownCount ? WordState.KNOWN : WordState.UNKNOWN))
                .collect(toList());
    }
}
