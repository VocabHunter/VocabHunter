/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.gui.dialogues.FileFormatType;
import io.github.vocabhunter.gui.model.FilterFileMode;

import java.util.EnumMap;
import java.util.Map;

public final class FileFormatTypeTool {
    private static final Map<FileFormatType, FilterFileMode> MAP = buildMap();

    private FileFormatTypeTool() {
        // Prevent instantiation - all methods are static
    }

    public static FilterFileMode getMode(final FileFormatType type) {
        FilterFileMode mode = MAP.get(type);

        if (mode == null) {
            throw new VocabHunterException("Unsupported type " + type);
        } else {
            return mode;
        }
    }

    private static Map<FileFormatType, FilterFileMode> buildMap() {
        Map<FileFormatType, FilterFileMode> map = new EnumMap<>(FileFormatType.class);

        map.put(FileFormatType.DOCUMENT, FilterFileMode.DOCUMENT);
        map.put(FileFormatType.SPREADSHEET, FilterFileMode.EXCEL);
        map.put(FileFormatType.SESSION, FilterFileMode.SESSION_KNOWN);

        return map;
    }
}
