/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.gui.i18n.I18nKey;

import static io.github.vocabhunter.gui.i18n.I18nKey.*;

public enum FileDialogueType {
    NEW_SESSION(FILE_NEW),
    OPEN_SESSION(FILE_OPEN),
    SAVE_SESSION(FILE_SAVE),
    EXPORT_SELECTION(FILE_EXPORT),
    OPEN_WORD_LIST(FILE_EXCLUDE);

    private final I18nKey titleKey;

    FileDialogueType(final I18nKey titleKey) {
        this.titleKey = titleKey;
    }

    public I18nKey getTitleKey() {
        return titleKey;
    }
}
