/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

import java.io.File;
import java.util.List;
import java.util.function.BiFunction;

public enum  FileDialogueType {
    NEW_SESSION("Select File To Analyse", FileFilters.INPUT_DOCUMENTS, FileChooser::showOpenDialog),
    OPEN_SESSION("Open", FileFilters.SESSIONS_OR_ANY, FileChooser::showOpenDialog),
    SAVE_SESSION("Save", FileFilters.SESSIONS, FileChooser::showSaveDialog),
    EXPORT_SELECTION("Export Selection", FileFilters.EXPORTS, FileChooser::showSaveDialog);

    private final String title;

    private final List<ExtensionFilter> filters;

    private final BiFunction<FileChooser, Window, File> openFunction;

    FileDialogueType(final String title, final List<ExtensionFilter> filters, final BiFunction<FileChooser, Window, File> openFunction) {
        this.title = title;
        this.filters = filters;
        this.openFunction = openFunction;
    }

    public File showChooser(final Window window) {
        FileChooser chooser = new FileChooser();

        chooser.setTitle(title);
        chooser.getExtensionFilters().addAll(filters);

        return openFunction.apply(chooser, window);
    }
}
