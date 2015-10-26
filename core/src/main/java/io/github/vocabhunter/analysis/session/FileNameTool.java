/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileNameTool {
    public static final String SESSION_SUFFIX = ".wordy";

    public static final String EXPORT_SUFFIX = ".txt";

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
        String filename = file.getFileName().toString();

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
}
