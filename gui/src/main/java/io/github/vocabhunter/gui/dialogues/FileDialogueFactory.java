/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import javafx.stage.Window;

public interface FileDialogueFactory {
    FileDialogue create(FileDialogueType type, Window stage);
}
