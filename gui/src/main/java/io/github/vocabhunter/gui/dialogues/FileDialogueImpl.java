/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.gui.settings.SettingsManager;
import javafx.stage.Stage;

import java.nio.file.Path;

public class FileDialogueImpl implements FileDialogue {
    private final FileDialogueType type;

    private final Stage stage;

    private final SettingsManager settingsManager;

    private Path selectedFile;

    public FileDialogueImpl(final FileDialogueType type, final Stage stage, final SettingsManager settingsManager) {
        this.stage = stage;
        this.type = type;
        this.settingsManager = settingsManager;
    }

    @Override
    public void showChooser() {
        selectedFile = type.showChooser(stage, settingsManager);
    }

    @Override
    public boolean isFileSelected() {
        return selectedFile != null;
    }

    @Override
    public Path getSelectedFile() {
        return selectedFile;
    }
}
