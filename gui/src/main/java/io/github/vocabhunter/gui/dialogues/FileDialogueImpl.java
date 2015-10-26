/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class FileDialogueImpl implements FileDialogue {
    private final FileDialogueType type;

    private final Stage stage;

    private Path selectedFile;

    public FileDialogueImpl(final FileDialogueType type, final Stage stage) {
        this.stage = stage;
        this.type = type;
    }

    @Override
    public void showChooser() {
        File file = type.showChooser(stage);

        if (file != null) {
            selectedFile = file.toPath();
        }
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
