/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.gui.dialogues.FileFormatType;
import io.github.vocabhunter.gui.model.FilterFileMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileFormatTypeToolTest {
    @Test
    public void testDocument() {
        validate(FileFormatType.DOCUMENT, FilterFileMode.DOCUMENT);
    }

    @Test
    public void testSpreadsheet() {
        validate(FileFormatType.SPREADSHEET, FilterFileMode.EXCEL);
    }

    @Test
    public void testSession() {
        validate(FileFormatType.SESSION, FilterFileMode.SESSION_KNOWN);
    }

    @Test
    public void testAll() {
        assertThrows(VocabHunterException.class, () -> FileFormatTypeTool.getMode(FileFormatType.ALL));
    }

    private void validate(final FileFormatType type, final FilterFileMode mode) {
        FilterFileMode actual = FileFormatTypeTool.getMode(type);

        assertEquals(mode, actual);
    }
}
