/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocumentGridReaderTest {
    private static final List<GridLine> EXPECTED = GridTestTool.documentLines();

    private final GridReader target = new DocumentGridReaderImpl();

    @Test
    public void testEmpty() throws Exception {
        validate("empty.txt", Collections.emptyList());
    }

    @Test
    public void testTxt() throws Exception {
        validate("test.txt", EXPECTED);
    }

    private void validate(final String name, final List<GridLine> expected) throws Exception {
        Path file = GridTestTool.getFile(name);

        List<GridLine> result = target.readGrid(file, "Rejected1"::equals);

        assertEquals(expected, result, "Validate " + name);
    }
}
