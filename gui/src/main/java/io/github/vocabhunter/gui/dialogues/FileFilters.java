/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.analysis.session.FileNameTool;
import javafx.stage.FileChooser.ExtensionFilter;

import java.util.List;

import static io.github.vocabhunter.analysis.core.CollectionTool.listOf;

public final class FileFilters {
    private static final ExtensionFilter FILTER_ALL = new ExtensionFilter("All Files", "*.*");

    private static final ExtensionFilter FILTER_TEXT = new ExtensionFilter("Text Files", "*.txt");

    private static final ExtensionFilter FILTER_ANY_TEXT = new ExtensionFilter("Text Files", "*.txt", "*.rtf");

    private static final ExtensionFilter FILTER_PDF = new ExtensionFilter("PDF Files", "*.pdf");

    private static final ExtensionFilter FILTER_OFFICE = new ExtensionFilter("Office Documents", "*.doc", "*.docx", "*.odt", "*.pages");

    private static final ExtensionFilter FILTER_EBOOK = new ExtensionFilter("EBooks", "*.epub");

    private static final ExtensionFilter FILTER_SESSION = new ExtensionFilter("VocabHunter Session Files", "*" + FileNameTool.SESSION_SUFFIX);

    public static final List<ExtensionFilter> INPUT_DOCUMENTS = listOf(FILTER_ANY_TEXT, FILTER_PDF, FILTER_OFFICE, FILTER_EBOOK, FILTER_ALL);

    public static final List<ExtensionFilter> SESSIONS = listOf(FILTER_SESSION);

    public static final List<ExtensionFilter> SESSIONS_OR_ANY = listOf(FILTER_SESSION, FILTER_ALL);

    public static final List<ExtensionFilter> EXPORTS = listOf(FILTER_TEXT);

    private FileFilters() {
        // Prevent instantiation - all members are static
    }
}
