/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.analysis.session.FileNameTool;
import javafx.stage.FileChooser.ExtensionFilter;

import java.util.List;
import java.util.stream.Stream;

import static io.github.vocabhunter.analysis.core.CollectionTool.listOf;
import static java.util.stream.Collectors.toList;

public final class FileFilters {
    private static final List<String> EXTENSIONS_ANY_TEXT = listOf("*.txt", "*.rtf");

    private static final List<String> EXTENSIONS_PDF = listOf("*.pdf");

    private static final List<String> EXTENSIONS_OFFICE = listOf("*.doc", "*.docx", "*.odt", "*.pages");

    private static final List<String> EXTENSIONS_EBOOK = listOf( "*.epub");

    private static final List<String> EXTENSIONS_DOCUMENT = Stream.of(EXTENSIONS_ANY_TEXT, EXTENSIONS_PDF, EXTENSIONS_OFFICE, EXTENSIONS_EBOOK)
        .flatMap(List::stream)
        .collect(toList());

    private static final ExtensionFilter FILTER_ALL = new ExtensionFilter("All Files", "*.*");

    private static final ExtensionFilter FILTER_TEXT = new ExtensionFilter("Text Files", "*.txt");

    private static final ExtensionFilter FILTER_DOCUMENT = new ExtensionFilter("Documents", EXTENSIONS_DOCUMENT);

    private static final ExtensionFilter FILTER_ANY_TEXT = new ExtensionFilter("Text Files", EXTENSIONS_ANY_TEXT);

    private static final ExtensionFilter FILTER_PDF = new ExtensionFilter("PDF Files", EXTENSIONS_PDF);

    private static final ExtensionFilter FILTER_OFFICE = new ExtensionFilter("Office Documents", EXTENSIONS_OFFICE);

    private static final ExtensionFilter FILTER_EBOOK = new ExtensionFilter("EBooks", EXTENSIONS_EBOOK);

    private static final ExtensionFilter FILTER_SESSION = new ExtensionFilter("VocabHunter Session Files", "*" + FileNameTool.SESSION_SUFFIX);

    public static final List<ExtensionFilter> INPUT_DOCUMENTS = listOf(FILTER_DOCUMENT, FILTER_ANY_TEXT, FILTER_PDF, FILTER_OFFICE, FILTER_EBOOK, FILTER_ALL);

    public static final List<ExtensionFilter> SESSIONS = listOf(FILTER_SESSION);

    public static final List<ExtensionFilter> SESSIONS_OR_ANY = listOf(FILTER_SESSION);

    public static final List<ExtensionFilter> EXPORTS = listOf(FILTER_TEXT);

    private FileFilters() {
        // Prevent instantiation - all members are static
    }
}
