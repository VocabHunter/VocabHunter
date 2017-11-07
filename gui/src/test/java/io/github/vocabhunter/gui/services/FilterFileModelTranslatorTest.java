/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.services;

import io.github.vocabhunter.analysis.core.CoreTool;
import io.github.vocabhunter.analysis.settings.BaseListedFile;
import io.github.vocabhunter.analysis.settings.DocumentListedFile;
import io.github.vocabhunter.analysis.settings.ExcelListedFile;
import io.github.vocabhunter.analysis.settings.SessionListedFile;
import io.github.vocabhunter.gui.model.FilterFileMode;
import io.github.vocabhunter.gui.model.FilterFileModel;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterFileModelTranslatorTest {
    private static final Path FILE = Paths.get("test");

    private static final Set<Integer> COLUMNS_EMPTY = emptySet();

    private static final Set<Integer> COLUMNS_DOCUMENT = Collections.singleton(0);

    private static final Set<Integer> COLUMNS_EXCEL = new TreeSet<>(CoreTool.listOf(1, 3));

    private final FilterFileModelTranslator target = new FilterFileModelTranslatorImpl();

    @Test
    public void testToSessionKnown() {
        SessionListedFile file = new SessionListedFile(FILE, false);

        validateToModel(file, FilterFileMode.SESSION_KNOWN, COLUMNS_EMPTY);
    }

    @Test
    public void testToSessionSeen() {
        SessionListedFile file = new SessionListedFile(FILE, true);

        validateToModel(file, FilterFileMode.SESSION_SEEN, COLUMNS_EMPTY);
    }

    @Test
    public void testToExcel() {
        ExcelListedFile file = new ExcelListedFile(FILE, COLUMNS_EXCEL);

        validateToModel(file, FilterFileMode.EXCEL, COLUMNS_EXCEL);
    }

    @Test
    public void testToDocument() {
        DocumentListedFile file = new DocumentListedFile(FILE);

        validateToModel(file, FilterFileMode.DOCUMENT, COLUMNS_DOCUMENT);
    }

    private void validateToModel(final BaseListedFile file, final FilterFileMode mode, final Set<Integer> columns) {
        FilterFileModel model = target.toModel(file);

        assertEquals(FILE, model.getFile());
        assertEquals(mode, model.getMode());
        assertEquals(columns, model.getColumns());
    }

    @Test
    public void testFromSessionKnown() {
        FilterFileModel model = new FilterFileModel(FILE, FilterFileMode.SESSION_KNOWN, COLUMNS_EMPTY);

        validateFromModel(model, new SessionListedFile(FILE, false));
    }

    @Test
    public void testFromSessionSeen() {
        FilterFileModel model = new FilterFileModel(FILE, FilterFileMode.SESSION_SEEN, COLUMNS_EMPTY);

        validateFromModel(model, new SessionListedFile(FILE, true));
    }

    @Test
    public void testFromExcel() {
        FilterFileModel model = new FilterFileModel(FILE, FilterFileMode.EXCEL, COLUMNS_EXCEL);

        validateFromModel(model, new ExcelListedFile(FILE, COLUMNS_EXCEL));
    }

    @Test
    public void testFromDocument() {
        FilterFileModel model = new FilterFileModel(FILE, FilterFileMode.DOCUMENT, COLUMNS_DOCUMENT);

        validateFromModel(model, new DocumentListedFile(FILE));
    }

    private void validateFromModel(final FilterFileModel model, final BaseListedFile expected) {
        BaseListedFile actual = target.fromModel(model);

        assertEquals(expected, actual);
    }
}
