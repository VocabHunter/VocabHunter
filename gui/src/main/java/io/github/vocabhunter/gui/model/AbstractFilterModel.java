/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.core.CoreConstants;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableNumberValue;

import java.nio.file.Path;
import java.text.NumberFormat;

import static io.github.vocabhunter.analysis.session.FileNameTool.filename;

public abstract class AbstractFilterModel {
    public static final String ERROR = "No words selected";

    private final SimpleObjectProperty<Path> file = new SimpleObjectProperty<>();

    private final SimpleStringProperty filename = new SimpleStringProperty();

    private final SimpleBooleanProperty error = new SimpleBooleanProperty();

    private final SimpleStringProperty countDescription = new SimpleStringProperty();

    protected AbstractFilterModel(final Path file) {
        this.file.set(file);
    }

    protected void replaceContent(final Path file) {
        this.file.set(file);
    }

    protected void bindValues() {
        ObservableNumberValue count = countValue();
        StringBinding countText = Bindings.createStringBinding(() -> formatTotalWords(count), count);
        StringBinding filenameText = Bindings.createStringBinding(() -> filename(file.get()), file);

        error.bind(Bindings.equal(count, 0));
        countDescription.bind(Bindings.when(error).then(ERROR).otherwise(countText));
        filename.bind(filenameText);
    }

    private String formatTotalWords(final ObservableNumberValue count) {
        return NumberFormat.getIntegerInstance(CoreConstants.LOCALE).format(count.getValue());
    }

    protected abstract ObservableNumberValue countValue();

    public Path getFile() {
        return file.get();
    }

    public ReadOnlyStringProperty filenameProperty() {
        return filename;
    }

    public String getFilename() {
        return filename.get();
    }

    public ReadOnlyStringProperty countDescriptionProperty() {
        return countDescription;
    }

    public String getCountDescription() {
        return countDescription.get();
    }

    public ReadOnlyBooleanProperty errorProperty() {
        return error;
    }

    public boolean isError() {
        return error.get();
    }
}
