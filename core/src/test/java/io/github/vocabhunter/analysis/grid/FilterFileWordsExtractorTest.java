/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.grid;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.session.SessionWordsTool;
import io.github.vocabhunter.analysis.settings.BaseListedFile;
import io.github.vocabhunter.analysis.settings.DocumentListedFile;
import io.github.vocabhunter.analysis.settings.ExcelListedFile;
import io.github.vocabhunter.analysis.settings.SessionListedFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static io.github.vocabhunter.analysis.grid.GridTestTool.acceptedCell;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class FilterFileWordsExtractorTest {
    private static final Path FILE = Paths.get("test");

    private static final Set<Integer> SPREADSHEET_COLUMNS = new TreeSet<>(listOf(2, 3));

    private static final SessionListedFile FILE_SESSION_KNOWN = new SessionListedFile(FILE, false);

    private static final SessionListedFile FILE_SESSION_SEEN = new SessionListedFile(FILE, true);

    private static final ExcelListedFile FILE_EXCEL = new ExcelListedFile(FILE, SPREADSHEET_COLUMNS);

    private static final DocumentListedFile FILE_DOCUMENT = new DocumentListedFile(FILE);

    private static final List<String> WORDS = listOf("WORD1", "WORD2");

    private static final List<GridLine> LINES = listOf(new GridLine(acceptedCell("test")));

    private static final TextGrid GRID = new TextGrid(LINES, emptyList());

    @Mock
    private SessionWordsTool sessionWordsTool;

    @Mock
    private GridWordsExtractor gridWordsExtractor;

    @Mock
    private TextGridManager textGridManager;

    @Mock
    private BaseListedFile unsupportedFile;

    @InjectMocks
    private FilterFileWordsExtractorImpl target;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSessionKnown() {
        when(sessionWordsTool.knownWords(FILE)).thenReturn(WORDS);

        validate(FILE_SESSION_KNOWN);
    }

    @Test
    public void testSessionSeen() {
        when(sessionWordsTool.seenWords(FILE)).thenReturn(WORDS);

        validate(FILE_SESSION_SEEN);
    }

    @Test
    public void testExcel() {
        when(textGridManager.readExcel(FILE)).thenReturn(GRID);
        when(gridWordsExtractor.words(LINES, SPREADSHEET_COLUMNS)).thenReturn(WORDS);

        validate(FILE_EXCEL);
    }

    @Test
    public void testDocument() {
        when(textGridManager.readDocument(FILE)).thenReturn(GRID);
        when(gridWordsExtractor.words(LINES, singleton(0))).thenReturn(WORDS);

        validate(FILE_DOCUMENT);
    }

    @Test
    public void testUnknownType() {
        assertThrows(VocabHunterException.class, () -> target.extract(unsupportedFile));
    }

    private void validate(final BaseListedFile listedFile) {
        List<String> result = target.extract(listedFile);

        assertEquals(result, WORDS);
    }
}
