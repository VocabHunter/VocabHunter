/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.gui.dialogues.FileDialogueType;
import io.github.vocabhunter.gui.dialogues.FileFormatType;

import java.nio.file.Path;

public interface GuiTestValidator {
    void validateWebPage(final String page);

    void validateSavedSession(Path file, final String name);

    void validateExportFile(Path file);

    void setUpFileDialogue(FileDialogueType dialogueType, final FileFormatType fileType, String file);

    void setUpFileDialogue(FileDialogueType dialogueType, final FileFormatType fileType, Path file);
}
