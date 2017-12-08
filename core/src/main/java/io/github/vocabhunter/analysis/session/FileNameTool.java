/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.core.CoreConstants;
import io.github.vocabhunter.analysis.core.VocabHunterException;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileNameTool {
    public static final String SESSION_SUFFIX = ".wordy";

    private static final String EXPORT_SUFFIX = ".txt";

    private FileNameTool() {
        // Prevent instantiation - all methods are static
    }

    public static Path ensureSessionFileHasSuffix(final Path file) {
        return ensureFileHasSuffix(file, SESSION_SUFFIX);
    }

    public static Path ensureExportFileHasSuffix(final Path file) {
        return ensureFileHasSuffix(file, EXPORT_SUFFIX);
    }

    private static Path ensureFileHasSuffix(final Path file, final String suffix) {
        String filename = filename(file);

        if (!filename.contains(".")) {
            String newFilename = filename + suffix;
            Path parent = file.getParent();

            if (parent == null) {
                return Paths.get(newFilename);
            } else {
                return parent.resolve(newFilename);
            }
        }

        return file;
    }

    public static boolean isSessionFile(final Path file) {
        return filename(file).toLowerCase(CoreConstants.LOCALE).endsWith(SESSION_SUFFIX);
    }

    public static String filename(final Path file) {
        Path fileName = file.getFileName();

        if (fileName == null) {
            throw new VocabHunterException("Empty file path");
        } else {
            return fileName.toString();
        }
    }
}
