/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import org.junit.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExcelGridReaderTest {
    private static final List<GridLine> EXPECTED = GridTestTool.normalisedGridLines();

    private final GridReader target = new ExcelGridReaderImpl();

    @Test
    public void testXls() throws Exception {
        validate("test.xls");
    }

    @Test
    public void testXlsx() throws Exception {
        validate("test.xlsx");
    }

    private void validate(final String name) throws Exception {
        Path file = GridTestTool.getFile(name);

        List<GridLine> result = target.readGrid(file, "B1"::equals);

        assertEquals("Validate " + name, EXPECTED, result);
    }
}
