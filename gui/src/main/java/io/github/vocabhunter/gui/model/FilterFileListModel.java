/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class FilterFileListModel {
    private final ObservableList<FilterFileModel> files;

    public FilterFileListModel(final List<FilterFileModel> files) {
        this.files = FXCollections.observableArrayList(FilterFileModel.PROPERTY_EXTRACTOR);
        this.files.addAll(files);
    }

    public ObservableList<FilterFileModel> getFiles() {
        return files;
    }

    public void removeIfExists(final FilterFileModel fileModel) {
        files.removeIf(f -> f != fileModel && f.getFile().equals(fileModel.getFile()));
    }

    public void addFile(final FilterFileModel fileModel) {
        files.add(fileModel);
    }

    public void remove(final FilterFileModel file) {
        files.remove(file);
    }
}
