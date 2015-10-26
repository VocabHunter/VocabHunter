/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.factory;

import io.github.vocabhunter.gui.dialogues.FileDialogue;
import io.github.vocabhunter.gui.dialogues.FileDialogueType;
import javafx.stage.Stage;

public interface FileDialogueFactory {
    FileDialogue create(final FileDialogueType type, final Stage stage);
}
