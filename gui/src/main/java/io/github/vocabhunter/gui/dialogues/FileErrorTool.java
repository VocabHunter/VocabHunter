/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public final class FileErrorTool {
    private static final Logger LOG = LoggerFactory.getLogger(FileErrorTool.class);

    private FileErrorTool() {
        // Prevent instantiation - all methods are static
    }

    public static void open(final Path file, final RuntimeException e) {
        handleFileError(file, e, "Open File Error", "Couldn't open file '%s'", "Unable to open file '{}'");
    }

    public static void save(final Path file, final RuntimeException e) {
        handleFileError(file, e, "Save Session Error", "Couldn't save file '%s'", "Unable to save session file '{}'");
    }

    public static void export(final Path file, final RuntimeException e) {
        handleFileError(file, e, "Export Error", "Couldn't create export file '%s'", "Unable export to file '{}'");
    }

    private static void handleFileError(final Path file, final RuntimeException e, final String title, final String message, final String log) {
        LOG.info(log, file, e);

        ErrorDialogue dialogue = new ErrorDialogue(title, e, String.format(message, file.getFileName()));

        dialogue.showError();
    }
}
