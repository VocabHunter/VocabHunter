/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.gui.dialogues.FileDialogue;
import io.github.vocabhunter.gui.dialogues.FileDialogueFactory;
import io.github.vocabhunter.gui.dialogues.FileDialogueImpl;
import io.github.vocabhunter.gui.dialogues.FileDialogueType;
import io.github.vocabhunter.gui.settings.SettingsManager;
import javafx.stage.Stage;

public class FileDialogueFactoryImpl implements FileDialogueFactory {
    private final SettingsManager settingsManager;

    public FileDialogueFactoryImpl(final SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    @Override
    public FileDialogue create(final FileDialogueType type, final Stage stage) {
        return new FileDialogueImpl(type, stage, settingsManager);
    }
}
