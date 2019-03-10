/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.session.FileNameTool;
import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import javafx.stage.FileChooser.ExtensionFilter;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.github.vocabhunter.gui.dialogues.FileFormatExtensions.*;
import static io.github.vocabhunter.gui.i18n.I18nKey.*;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public enum FileFormatType {
    ALL(FILE_TYPE_ALL, "*.*"),
    TEXT(FILE_TYPE_TEXT, "*.txt"),
    DOCUMENT(FILE_TYPE_DOCUMENT, EXTENSIONS_DOCUMENT),
    ANY_TEXT(FILE_TYPE_ANY_TEXT, EXTENSIONS_ANY_TEXT),
    PDF(FILE_TYPE_PDF, EXTENSIONS_PDF),
    OFFICE(FILE_TYPE_OFFICE, EXTENSIONS_OFFICE),
    EBOOK(FILE_TYPE_EBOOK, EXTENSIONS_EBOOK),
    SESSION(FILE_TYPE_SESSION, "*" + FileNameTool.SESSION_SUFFIX),
    SPREADSHEET(FILE_TYPE_SPREADSHEET, EXTENSIONS_SPREADSHEET);

    public static final List<FileFormatType> TYPES_INPUT_DOCUMENTS = List.of(DOCUMENT, ANY_TEXT, PDF, OFFICE, EBOOK, ALL);

    public static final List<FileFormatType> TYPES_SESSIONS = List.of(SESSION);

    public static final List<FileFormatType> TYPES_EXPORTS = List.of(TEXT);

    public static final List<FileFormatType> TYPES_WORD_GRIDS = List.of(DOCUMENT, SPREADSHEET);

    private static final Map<List<String>, FileFormatType> TYPES = Stream.of(FileFormatType.values())
        .collect(toMap(FileFormatType::getExtensions, identity()));

    private final I18nKey descriptionKey;

    private final List<String> extensions;

    FileFormatType(final I18nKey descriptionKey, final String... extensions) {
        this(descriptionKey, List.of(extensions));
    }

    FileFormatType(final I18nKey descriptionKey, final List<String> extensions) {
        this.descriptionKey = descriptionKey;
        this.extensions = List.copyOf(extensions);
    }

    private List<String> getExtensions() {
        return extensions;
    }

    public ExtensionFilter buildFilter(final I18nManager i18nManager) {
        return new ExtensionFilter(i18nManager.text(descriptionKey), extensions);
    }

    public static FileFormatType getByFilter(final ExtensionFilter filter) {
        FileFormatType result = TYPES.get(filter.getExtensions());

        if (result == null) {
            throw new VocabHunterException("Unknown type " + filter.getDescription());
        } else {
            return result;
        }
    }
}
