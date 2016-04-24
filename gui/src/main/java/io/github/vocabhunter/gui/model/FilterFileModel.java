/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.session.FileNameTool;

import java.nio.file.Path;

public class FilterFileModel {
    private final Path file;

    private FilterFileMode mode = FilterFileMode.KNOWN;

    public FilterFileModel(final Path file) {
        this.file = file;
    }

    public FilterFileMode getMode() {
        return mode;
    }

    public void setMode(final FilterFileMode mode) {
        this.mode = mode;
    }

    public String getName() {
        return FileNameTool.filename(file);
    }

    public Path getFile() {
        return file;
    }
}
