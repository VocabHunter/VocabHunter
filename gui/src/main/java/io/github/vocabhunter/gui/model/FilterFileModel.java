/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.session.FileNameTool;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Callback;

import java.nio.file.Path;

public class FilterFileModel {
    public static final Callback<FilterFileModel, Observable[]> PROPERTY_EXTRACTOR
        = m -> new Observable[] {m.modeProperty};

    private final Path file;

    private SimpleObjectProperty<FilterFileMode> modeProperty;

    public FilterFileModel(final Path file) {
        this(file, FilterFileMode.KNOWN);
    }

    public FilterFileModel(final Path file, final FilterFileMode mode) {
        this.file = file;
        this.modeProperty = new SimpleObjectProperty<>(mode);
    }

    public FilterFileMode getMode() {
        return modeProperty.get();
    }

    public void setMode(final FilterFileMode mode) {
        modeProperty.set(mode);
    }

    public String getName() {
        return FileNameTool.filename(file);
    }

    public Path getFile() {
        return file;
    }
}
