/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.test.analysis.grid.GridTestTool;
import io.github.vocabhunter.test.core.TestFileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest
    @CsvSource({
        "test.xls",
        "test.xlsx"
    })
    public void testReadGrid(final String name) throws Exception {
        Path file = files.addCopy(name);

        List<GridLine> result = target.readGrid(file, "B1"::equals);

        assertEquals(EXPECTED, result, "Validate " + name);
    }
}
