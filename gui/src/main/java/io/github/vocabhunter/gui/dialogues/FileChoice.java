/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import java.nio.file.Path;

public final class FileChoice {
    private final Path file;

    private final FileFormatType type;

    public FileChoice(final Path file, final FileFormatType type) {
        this.file = file;
        this.type = type;
    }

    public Path getFile() {
        return file;
    }

    public FileFormatType getType() {
        return type;
    }
}
