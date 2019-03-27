/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static io.github.vocabhunter.gui.i18n.I18nKey.*;

public final class FileErrorTool {
    private static final Logger LOG = LoggerFactory.getLogger(FileErrorTool.class);

    private FileErrorTool() {
        // Prevent instantiation - all methods are static
    }

    public static void open(final I18nManager i18nManager, final Path file, final RuntimeException e) {
        handleFileError(i18nManager, file, e, ERROR_SESSION_OPEN_TITLE, ERROR_SESSION_OPEN_DETAILS, "Unable to open file '{}'");
    }

    public static void save(final I18nManager i18nManager, final Path file, final RuntimeException e) {
        handleFileError(i18nManager, file, e, ERROR_SESSION_SAVE_TITLE, ERROR_SESSION_SAVE_DETAILS, "Unable to save session file '{}'");
    }

    public static void export(final I18nManager i18nManager, final Path file, final RuntimeException e) {
        handleFileError(i18nManager, file, e, ERROR_SESSION_EXPORT_TITLE, ERROR_SESSION_EXPORT_DETAILS, "Unable export to file '{}'");
    }

    private static void handleFileError(final I18nManager i18nManager, final Path file, final RuntimeException e, final I18nKey titleKey, final I18nKey detailKey, final String log) {
        LOG.info(log, file, e);

        String message = i18nManager.text(detailKey, file.getFileName());
        ErrorDialogue dialogue = new ErrorDialogue(i18nManager, titleKey, e, message);

        dialogue.showError();
    }
}
