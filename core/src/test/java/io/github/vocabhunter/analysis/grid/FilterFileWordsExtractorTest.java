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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import static io.github.vocabhunter.test.analysis.grid.GridTestTool.acceptedCell;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FilterFileWordsExtractorTest {
    private static final Path FILE = Paths.get("test");

    private static final Set<Integer> SPREADSHEET_COLUMNS = Set.of(2, 3);

    private static final SessionListedFile FILE_SESSION_KNOWN = new SessionListedFile(FILE, false);

    private static final SessionListedFile FILE_SESSION_SEEN = new SessionListedFile(FILE, true);

    private static final ExcelListedFile FILE_EXCEL = new ExcelListedFile(FILE, SPREADSHEET_COLUMNS);

    private static final DocumentListedFile FILE_DOCUMENT = new DocumentListedFile(FILE);

    private static final List<String> WORDS = List.of("WORD1", "WORD2");

    private static final List<GridLine> LINES = List.of(new GridLine(acceptedCell("test")));

    private static final TextGrid GRID = new TextGrid(LINES, List.of());

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
        when(gridWordsExtractor.words(LINES, Set.of(0))).thenReturn(WORDS);

        validate(FILE_DOCUMENT);
    }

    @Test
    public void testUnknownType() {
        assertThrows(VocabHunterException.class, () -> target.extract(unsupportedFile));
    }

    private void validate(final BaseListedFile listedFile) {
        List<String> result = target.extract(listedFile);

        assertEquals(WORDS, result);
    }
}
