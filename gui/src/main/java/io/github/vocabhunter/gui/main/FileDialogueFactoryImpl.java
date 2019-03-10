/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.gui.dialogues.*;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.settings.SettingsManager;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;

import static io.github.vocabhunter.gui.dialogues.FileDialogueType.*;
import static io.github.vocabhunter.gui.dialogues.FileFormatType.*;
import static java.util.Collections.unmodifiableMap;

@Singleton
public class FileDialogueFactoryImpl implements FileDialogueFactory {
    private final I18nManager i18nManager;

    private final SettingsManager settingsManager;

    private final Map<FileDialogueType, Function<Window, FileDialogue>> creators = buildCreatorMap();

    @Inject
    public FileDialogueFactoryImpl(final I18nManager i18nManager, final SettingsManager settingsManager) {
        this.i18nManager = i18nManager;
        this.settingsManager = settingsManager;
    }

    @Override
    public FileDialogue create(final FileDialogueType type, final Window window) {
        Function<Window, FileDialogue> factory = creators.get(type);

        return factory.apply(window);
    }

    private FileDialogue createNewSessionDialogue(final Window window) {
        return new FileDialogueImpl(
            i18nManager, OPEN_SESSION, window, settingsManager, TYPES_INPUT_DOCUMENTS, FileChooser::showOpenDialog, SettingsManager::getDocumentsPath, SettingsManager::setDocumentsPath);
    }

    private FileDialogue createOpenSessionDialogue(final Window window) {
        return new FileDialogueImpl(
            i18nManager, OPEN_SESSION, window, settingsManager, TYPES_SESSIONS, FileChooser::showOpenDialog, SettingsManager::getSessionsPath, SettingsManager::setSessionsPath);
    }

    private FileDialogue createSaveSessionDialogue(final Window window) {
        return new FileDialogueImpl(
            i18nManager, SAVE_SESSION, window, settingsManager, TYPES_SESSIONS, FileChooser::showSaveDialog, SettingsManager::getSessionsPath, SettingsManager::setSessionsPath);
    }

    private FileDialogue createExportSelectionDialogue(final Window window) {
        return new FileDialogueImpl(
            i18nManager, EXPORT_SELECTION, window, settingsManager, TYPES_EXPORTS, FileChooser::showSaveDialog, SettingsManager::getExportPath, SettingsManager::setExportPath);
    }

    private FileDialogue createOpenWordListDialogue(final Window window) {
        return new FileDialogueImpl(
            i18nManager, OPEN_WORD_LIST, window, settingsManager, TYPES_WORD_GRIDS, FileChooser::showOpenDialog, SettingsManager::getWordListPath, SettingsManager::setWordListPath);
    }

    private Map<FileDialogueType, Function<Window, FileDialogue>> buildCreatorMap() {
        Map<FileDialogueType, Function<Window, FileDialogue>> map = new EnumMap<>(FileDialogueType.class);

        map.put(NEW_SESSION, this::createNewSessionDialogue);
        map.put(OPEN_SESSION, this::createOpenSessionDialogue);
        map.put(SAVE_SESSION, this::createSaveSessionDialogue);
        map.put(EXPORT_SELECTION, this::createExportSelectionDialogue);
        map.put(OPEN_WORD_LIST, this::createOpenWordListDialogue);

        return unmodifiableMap(map);
    }
}
