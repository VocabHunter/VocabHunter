/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.gui.settings.SettingsManager;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum  FileDialogueType {
    NEW_SESSION("Select File To Analyse", FileFilters.INPUT_DOCUMENTS, FileChooser::showOpenDialog, SettingsManager::getDocumentsPath, SettingsManager::setDocumentsPath),
    OPEN_SESSION("Open", FileFilters.SESSIONS_OR_ANY, FileChooser::showOpenDialog, SettingsManager::getSessionsPath, SettingsManager::setSessionsPath),
    SAVE_SESSION("Save", FileFilters.SESSIONS, FileChooser::showSaveDialog, SettingsManager::getSessionsPath, SettingsManager::setSessionsPath),
    EXPORT_SELECTION("Export Selection", FileFilters.EXPORTS, FileChooser::showSaveDialog, SettingsManager::getExportPath, SettingsManager::setExportPath);

    private final String title;

    private final List<ExtensionFilter> filters;

    private final BiFunction<FileChooser, Window, File> openFunction;

    private final Function<SettingsManager, Path> pathGetter;

    private final BiConsumer<SettingsManager, Path> pathSetter;

    FileDialogueType(final String title, final List<ExtensionFilter> filters, final BiFunction<FileChooser, Window, File> openFunction,
                     final Function<SettingsManager, Path> pathGetter, final BiConsumer<SettingsManager, Path> pathSetter) {
        this.title = title;
        this.filters = filters;
        this.openFunction = openFunction;
        this.pathGetter = pathGetter;
        this.pathSetter = pathSetter;
    }

    public Path showChooser(final Window window, final SettingsManager settingsManager) {
        FileChooser chooser = new FileChooser();

        chooser.setTitle(title);
        chooser.getExtensionFilters().addAll(filters);
        File initialDirectory = pathGetter.apply(settingsManager).toFile();
        chooser.setInitialDirectory(initialDirectory);

        File result = openFunction.apply(chooser, window);

        if (result == null) {
            return null;
        } else {
            Path file = result.toPath();

            pathSetter.accept(settingsManager, file.getParent());

            return file;
        }
   }
}
