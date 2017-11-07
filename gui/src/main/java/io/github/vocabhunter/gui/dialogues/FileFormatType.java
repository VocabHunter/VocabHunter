/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.session.FileNameTool;
import javafx.stage.FileChooser.ExtensionFilter;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static io.github.vocabhunter.gui.dialogues.FileFormatExtensions.*;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public enum FileFormatType {
    ALL("All Files", "*.*"),
    TEXT("Text Files", "*.txt"),
    DOCUMENT("Documents", EXTENSIONS_DOCUMENT),
    ANY_TEXT("Text Files", EXTENSIONS_ANY_TEXT),
    PDF("PDF Files", EXTENSIONS_PDF),
    OFFICE("Office Documents", EXTENSIONS_OFFICE),
    EBOOK("EBooks", EXTENSIONS_EBOOK),
    SESSION("VocabHunter Session Files", "*" + FileNameTool.SESSION_SUFFIX),
    SPREADSHEET("Spreadsheets", EXTENSIONS_SPREADSHEET);

    static final List<FileFormatType> TYPES_INPUT_DOCUMENTS = listOf(DOCUMENT, ANY_TEXT, PDF, OFFICE, EBOOK, ALL);

    static final List<FileFormatType> TYPES_SESSIONS = listOf(SESSION);

    static final List<FileFormatType> TYPES_EXPORTS = listOf(TEXT);

    static final List<FileFormatType> TYPES_WORD_GRIDS = listOf(DOCUMENT, SPREADSHEET);

    private static final Map<ExtensionFilter, FileFormatType> TYPES = Stream.of(FileFormatType.values())
        .collect(toMap(FileFormatType::getFilter, identity()));

    private final ExtensionFilter filter;

    FileFormatType(final String description, final String... extensions) {
        this.filter = new ExtensionFilter(description, extensions);
    }

    FileFormatType(final String description, final List<String> extensions) {
        this.filter = new ExtensionFilter(description, extensions);
    }

    public ExtensionFilter getFilter() {
        return filter;
    }

    public static FileFormatType getByFilter(final ExtensionFilter filter) {
        FileFormatType result = TYPES.get(filter);

        if (result == null) {
            throw new VocabHunterException("Unknown type " + filter.getDescription());
        } else {
            return result;
        }
    }
}
