/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class FilterModelTestTool {
    public static final String FILENAME_1 = "file1";

    public static final String FILENAME_2 = "file2";

    public static final Path FILE_1 = Paths.get("directory", FILENAME_1);

    public static final Path FILE_2 = Paths.get("directory", FILENAME_2);

    private FilterModelTestTool() {
        // Prevent instantiation - all members are static
    }

    public static void validateCommon(final AbstractFilterModel target, final Path file, final String filename, final int count, final boolean isError) {
        assertAll(
            () -> assertEquals(file, target.getFile(), "File"),
            () -> assertEquals(filename, target.getFilename(), "Filename"),
            () -> assertEquals(isError, target.isError(), "Error state"),
            () -> assertEquals(count, target.getWordCount(), "Count")
        );
    }
}
