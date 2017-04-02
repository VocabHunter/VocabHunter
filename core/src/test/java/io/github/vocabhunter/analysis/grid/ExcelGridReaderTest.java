/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.test.utils.TestFileManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExcelGridReaderTest {
    private static final List<GridLine> EXPECTED = GridTestTool.normalisedGridLines();

    private TestFileManager files;

    private final GridReader target = new ExcelGridReaderImpl();

    @Before
    public void setUp() throws Exception {
        files = new TestFileManager(ExcelGridReaderTest.class);
    }

    @After
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

        assertEquals("Validate " + name, EXPECTED, result);
    }
}
