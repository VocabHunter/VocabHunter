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
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterSessionModel {
    public static final String ERROR = "No words selected";

    private final long knownCount;

    private final long seenCount;

    private final SimpleBooleanProperty includeUnknown = new SimpleBooleanProperty();

    private final SimpleBooleanProperty error = new SimpleBooleanProperty();

    private final SimpleStringProperty countDescription = new SimpleStringProperty();

    private final ObservableList<FilterSessionWord> seenWords = FXCollections.observableArrayList();

    public FilterSessionModel(final List<? extends MarkedWord> words) {
        Map<WordState, Long> counts = words.stream()
            .collect(Collectors.groupingBy(MarkedWord::getState, Collectors.counting()));

        knownCount = counts.getOrDefault(WordState.KNOWN, 0L);
        seenCount = counts.getOrDefault(WordState.UNKNOWN, 0L) + knownCount;
        words.stream()
            .filter(w -> w.getState() != WordState.UNSEEN)
            .map(FilterSessionWord::new)
            .forEach(seenWords::add);

        bindValues();
    }

    private void bindValues() {
        NumberBinding count = Bindings.when(includeUnknown).then(seenCount).otherwise(knownCount);
        StringBinding countText = Bindings.createStringBinding(() -> formatTotalWords(count), count);

        error.bind(count.isEqualTo(0));
        countDescription.bind(Bindings.when(error).then(ERROR).otherwise(countText));
    }

    private String formatTotalWords(final NumberBinding count) {
        return NumberFormat.getIntegerInstance(CoreConstants.LOCALE).format(count.getValue());
    }

    public long getKnownCount() {
        return knownCount;
    }

    public long getSeenCount() {
        return seenCount;
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
}
