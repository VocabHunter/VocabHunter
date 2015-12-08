/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.event;

import java.nio.file.Path;

public final class ExternalOpenFileEvent {
    private final Path file;

    public ExternalOpenFileEvent(final Path file) {
        this.file = file;
    }

    public Path getFile() {
        return file;
    }
}
