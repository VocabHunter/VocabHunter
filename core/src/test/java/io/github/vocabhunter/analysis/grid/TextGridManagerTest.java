/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TextGridManagerTest {
    private static final Path FILE = Paths.get("test");

    private static final TextGrid GRID = new TextGrid(emptyList(), emptyList());

    @Mock
    private List<GridLine> lines;

    @Mock
    private DocumentGridReader documentGridReader;

    @Mock
    private ExcelGridReader excelGridReader;

    @Mock
    private TextGridBuilder textGridBuilder;

    @InjectMocks
    private TextGridManagerImpl target;

    @Before
    public void setUp() {
        when(textGridBuilder.build(lines)).thenReturn(GRID);
    }

    @Test
    public void testReadDocument() {
        when(documentGridReader.readGrid(FILE, TextGridManagerImpl.FILTER)).thenReturn(lines);

        TextGrid result = target.readDocument(FILE);

        assertEquals(GRID, result);
    }

    @Test
    public void testReadExcel() {
        when(excelGridReader.readGrid(FILE, TextGridManagerImpl.FILTER)).thenReturn(lines);

        TextGrid result = target.readExcel(FILE);

        assertSame(GRID, result);
    }
}
