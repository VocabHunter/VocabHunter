/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class BaseFilterModelTest {
    protected static final String FILENAME_1 = "file1";

    protected static final String FILENAME_2 = "file2";

    protected static final Path FILE_1 = Paths.get("directory", FILENAME_1);

    protected static final Path FILE_2 = Paths.get("directory", FILENAME_2);

    protected void validateCommon(final AbstractFilterModel target, final Path file, final String filename, final String countDescription, final boolean isError) {
        assertEquals("File", file, target.getFile());
        assertEquals("Filename", filename, target.getFilename());
        assertEquals("Error state", isError, target.isError());
        assertEquals("Count description", countDescription, target.getCountDescription());
    }
}
