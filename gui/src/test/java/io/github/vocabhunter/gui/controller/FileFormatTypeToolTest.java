/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.gui.dialogues.FileFormatType;
import io.github.vocabhunter.gui.model.FilterFileMode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    @Test(expected = VocabHunterException.class)
    public void testAll() {
        FileFormatTypeTool.getMode(FileFormatType.ALL);
    }

    private void validate(final FileFormatType type, final FilterFileMode mode) {
        FilterFileMode actual = FileFormatTypeTool.getMode(type);

        assertEquals(mode, actual);
    }
}
