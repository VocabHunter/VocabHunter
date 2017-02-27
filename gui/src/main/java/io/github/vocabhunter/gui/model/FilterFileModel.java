/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.session.FileNameTool;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.util.Callback;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class FilterFileModel {
    public static final Callback<FilterFileModel, Observable[]> PROPERTY_EXTRACTOR
        = m -> new Observable[] {m.file, m.modeProperty};

    private final SimpleObjectProperty<Path> file;

    private final SimpleObjectProperty<FilterFileMode> modeProperty;

    private final ObservableSet<Integer> columns;

    public FilterFileModel(final Path file) {
        this(file, FilterFileMode.SESSION_KNOWN);
    }

    public FilterFileModel(final Path file, final FilterFileMode mode) {
        this(file, mode, Collections.emptySet());
    }

    public FilterFileModel(final Path file, final FilterFileMode mode, final Set<Integer> columns) {
        this.file = new SimpleObjectProperty<>(file);
        this.modeProperty = new SimpleObjectProperty<>(mode);
        this.columns = FXCollections.observableSet(columns);
    }

    public FilterFileMode getMode() {
        return modeProperty.get();
    }

    public void setMode(final FilterFileMode mode) {
        modeProperty.set(mode);
    }

    public String getName() {
        return FileNameTool.filename(file.get());
    }

    public Path getFile() {
        return file.get();
    }

    public void setFile(final Path file) {
        this.file.set(file);
    }

    public Set<Integer> getColumns() {
        return new TreeSet<>(columns);
    }

    public void setColumns(final Set<Integer> columns) {
        this.columns.clear();
        this.columns.addAll(columns);
    }
}
