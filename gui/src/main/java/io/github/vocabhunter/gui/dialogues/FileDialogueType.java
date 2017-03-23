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
import java.util.stream.Collectors;

public enum FileDialogueType {
    NEW_SESSION("Select File To Analyse", FileFormatType.TYPES_INPUT_DOCUMENTS, FileChooser::showOpenDialog, SettingsManager::getDocumentsPath, SettingsManager::setDocumentsPath),
    OPEN_SESSION("Open", FileFormatType.TYPES_SESSIONS, FileChooser::showOpenDialog, SettingsManager::getSessionsPath, SettingsManager::setSessionsPath),
    SAVE_SESSION("Save", FileFormatType.TYPES_SESSIONS, FileChooser::showSaveDialog, SettingsManager::getSessionsPath, SettingsManager::setSessionsPath),
    EXPORT_SELECTION("Export Selection", FileFormatType.TYPES_EXPORTS, FileChooser::showSaveDialog, SettingsManager::getExportPath, SettingsManager::setExportPath),
    OPEN_WORD_LIST("Open list of words to exclude", FileFormatType.TYPES_WORD_GRIDS, FileChooser::showOpenDialog, SettingsManager::getWordListPath, SettingsManager::setWordListPath);

    private final String title;

    private final List<ExtensionFilter> filters;

    private final BiFunction<FileChooser, Window, File> openFunction;

    private final Function<SettingsManager, Path> pathGetter;

    private final BiConsumer<SettingsManager, Path> pathSetter;

    FileDialogueType(final String title, final List<FileFormatType> formats, final BiFunction<FileChooser, Window, File> openFunction,
                     final Function<SettingsManager, Path> pathGetter, final BiConsumer<SettingsManager, Path> pathSetter) {
        this.title = title;
        this.filters = formats.stream()
            .map(FileFormatType::getFilter)
            .collect(Collectors.toList());
        this.openFunction = openFunction;
        this.pathGetter = pathGetter;
        this.pathSetter = pathSetter;
    }

    public FileChoice showChooser(final Window window, final SettingsManager settingsManager) {
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
            ExtensionFilter selected = chooser.getSelectedExtensionFilter();
            FileFormatType type = FileFormatType.getByFilter(selected);

            pathSetter.accept(settingsManager, file.getParent());

            return new FileChoice(file, type);
        }
   }
}
