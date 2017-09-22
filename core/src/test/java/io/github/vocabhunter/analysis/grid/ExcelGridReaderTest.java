/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.test.utils.TestFileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExcelGridReaderTest {
    private static final List<GridLine> EXPECTED = GridTestTool.normalisedGridLines();

    private TestFileManager files;

    private final GridReader target = new ExcelGridReaderImpl();

    @BeforeEach
    public void setUp() throws Exception {
        files = new TestFileManager(ExcelGridReaderTest.class);
    }

    @AfterEach
    public void tearDown() throws Exception {
        files.cleanup();
    }

    @Test
    public void testXls() throws Exception {
        validate("test.xls");
    }

    @Test
    public void testXlsx() throws Exception {
        validate("test.xlsx");
    }

    private void validate(final String name) throws Exception {
        Path file = files.addCopy(name);

        List<GridLine> result = target.readGrid(file, "B1"::equals);

        assertEquals(EXPECTED, result, "Validate " + name);
    }
}
