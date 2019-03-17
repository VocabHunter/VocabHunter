/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableNumberValue;

import java.nio.file.Path;

import static io.github.vocabhunter.analysis.session.FileNameTool.filename;

public abstract class AbstractFilterModel {
    private final SimpleObjectProperty<Path> file = new SimpleObjectProperty<>();

    private final SimpleStringProperty filename = new SimpleStringProperty();

    private final SimpleBooleanProperty error = new SimpleBooleanProperty();

    protected AbstractFilterModel(final Path file) {
        this.file.set(file);
    }

    protected void replaceContent(final Path file) {
        this.file.set(file);
    }

    protected void bindValues() {
        ObservableNumberValue count = wordCountProperty();
        StringBinding filenameText = Bindings.createStringBinding(() -> filename(file.get()), file);

        error.bind(Bindings.equal(count, 0));
        filename.bind(filenameText);
    }

    public abstract ObservableNumberValue wordCountProperty();

    public int getWordCount() {
        return wordCountProperty().intValue();
    }

    public Path getFile() {
        return file.get();
    }

    public ReadOnlyStringProperty filenameProperty() {
        return filename;
    }

    public String getFilename() {
        return filename.get();
    }

    public ReadOnlyBooleanProperty errorProperty() {
        return error;
    }

    public boolean isError() {
        return error.get();
    }
}
