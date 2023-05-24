/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.gui.dialogues.FileDialogueType;
import io.github.vocabhunter.gui.dialogues.FileFormatType;
import io.github.vocabhunter.gui.i18n.I18nKey;

import java.nio.file.Path;

public interface GuiTestValidator {
    void validateWebPage(I18nKey key);

    void validateSavedSession(Path file, String name);

    void validateExportFile(Path file, String... lines);

    void validateClipboardContent(String expectedText);

    void setUpFileDialogue(FileDialogueType dialogueType, FileFormatType fileType, String file);

    void setUpFileDialogue(FileDialogueType dialogueType, FileFormatType fileType, Path file);
}
