/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import java.util.List;
import java.util.stream.Stream;

public final class FileFormatExtensions {
    static final List<String> EXTENSIONS_ANY_TEXT = List.of("*.txt", "*.rtf");

    static final List<String> EXTENSIONS_PDF = List.of("*.pdf");

    static final List<String> EXTENSIONS_OFFICE = List.of("*.doc", "*.docx", "*.odt", "*.pages");

    static final List<String> EXTENSIONS_EBOOK = List.of( "*.epub");

    static final List<String> EXTENSIONS_DOCUMENT = Stream.of(EXTENSIONS_ANY_TEXT, EXTENSIONS_PDF, EXTENSIONS_OFFICE, EXTENSIONS_EBOOK)
        .flatMap(List::stream)
        .toList();

    static final List<String> EXTENSIONS_SPREADSHEET = List.of( "*.xls", "*.xlsx");

    private FileFormatExtensions() {
        // Prevent instantiation - all members are static
    }
}
