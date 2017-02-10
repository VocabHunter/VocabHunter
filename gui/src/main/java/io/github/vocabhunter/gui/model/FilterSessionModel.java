/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.core.CoreConstants;
import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.marked.WordState;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.vocabhunter.analysis.session.FileNameTool.filename;

public class FilterSessionModel {
    public static final String ERROR = "No words selected";

    private final SimpleLongProperty knownCount = new SimpleLongProperty();

    private final SimpleLongProperty seenCount = new SimpleLongProperty();

    private final SimpleBooleanProperty includeUnknown = new SimpleBooleanProperty();

    private final SimpleBooleanProperty error = new SimpleBooleanProperty();

    private final SimpleStringProperty countDescription = new SimpleStringProperty();

    private final ObservableList<FilterSessionWord> seenWords = FXCollections.observableArrayList();

    private final SimpleObjectProperty<Path> file = new SimpleObjectProperty<>();

    private final SimpleStringProperty filename = new SimpleStringProperty();

    public FilterSessionModel(final Path file, final List<? extends MarkedWord> words) {
        setupValues(file, words);
        bindValues();
    }

    public void replaceContent(final Path file, final List<? extends MarkedWord> words) {
        setupValues(file, words);
    }

    private void setupValues(final Path file, final List<? extends MarkedWord> words) {
        Map<WordState, Long> counts = words.stream()
            .collect(Collectors.groupingBy(MarkedWord::getState, Collectors.counting()));

        knownCount.set(counts.getOrDefault(WordState.KNOWN, 0L));
        seenCount.set(counts.getOrDefault(WordState.UNKNOWN, 0L) + knownCount.get());
        seenWords.clear();
        words.stream()
            .filter(w -> w.getState() != WordState.UNSEEN)
            .map(FilterSessionWord::new)
            .forEach(seenWords::add);
        this.file.set(file);
    }

    private void bindValues() {
        NumberBinding count = Bindings.when(includeUnknown).then(seenCount).otherwise(knownCount);
        StringBinding countText = Bindings.createStringBinding(() -> formatTotalWords(count), count);
        StringBinding filenameText = Bindings.createStringBinding(() -> filename(file.get()), file);

        error.bind(count.isEqualTo(0));
        countDescription.bind(Bindings.when(error).then(ERROR).otherwise(countText));
        filename.bind(filenameText);
    }

    private String formatTotalWords(final NumberBinding count) {
        return NumberFormat.getIntegerInstance(CoreConstants.LOCALE).format(count.getValue());
    }

    public long getKnownCount() {
        return knownCount.get();
    }

    public long getSeenCount() {
        return seenCount.get();
    }

    public SimpleBooleanProperty includeUnknownProperty() {
        return includeUnknown;
    }

    public boolean isIncludeUnknown() {
        return includeUnknown.get();
    }

    public void setIncludeUnknown(final boolean isIncludeUnknown) {
        includeUnknown.set(isIncludeUnknown);
    }

    public ObservableList<FilterSessionWord> getSeenWords() {
        return seenWords;
    }

    public ReadOnlyBooleanProperty errorProperty() {
        return error;
    }

    public boolean isError() {
        return error.get();
    }

    public ReadOnlyStringProperty countDescriptionProperty() {
        return countDescription;
    }

    public String getCountDescription() {
        return countDescription.get();
    }

    public ReadOnlyObjectProperty<Path> fileProperty() {
        return file;
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
}
