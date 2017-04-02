/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import java.util.List;
import java.util.stream.Stream;

import static io.github.vocabhunter.analysis.core.CoreTool.listOf;
import static java.util.stream.Collectors.toList;

public final class FileFormatExtensions {
    public static final List<String> EXTENSIONS_ANY_TEXT = listOf("*.txt", "*.rtf");

    public static final List<String> EXTENSIONS_PDF = listOf("*.pdf");

    public static final List<String> EXTENSIONS_OFFICE = listOf("*.doc", "*.docx", "*.odt", "*.pages");

    public static final List<String> EXTENSIONS_EBOOK = listOf( "*.epub");

    public static final List<String> EXTENSIONS_DOCUMENT = Stream.of(EXTENSIONS_ANY_TEXT, EXTENSIONS_PDF, EXTENSIONS_OFFICE, EXTENSIONS_EBOOK)
        .flatMap(List::stream)
        .collect(toList());

    public static final List<String> EXTENSIONS_SPREADSHEET = listOf( "*.xls", "*.xlsx");

    private FileFormatExtensions() {
        // Prevent instantiation - all members are static
    }
}
