/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.analysis.file.FileStreamerTest;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExcelGridReaderTest {
    private static final List<GridLine> EXPECTED = GridTestTool.normalisedLines();

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
        Path file = getFile(name);

        List<GridLine> result = target.readGrid(file, "B1"::equals);

        assertEquals("Validate " + name, EXPECTED, result);
    }

    private Path getFile(final String fileName) throws Exception {
        URL resource = getResource(fileName);

        return Paths.get(resource.toURI());
    }

    private URL getResource(final String file) {
        return FileStreamerTest.class.getResource("/" + file);
    }
}
