/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.marked.WordState;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterSessionModel extends AbstractFilterModel {
    private final SimpleLongProperty knownCount = new SimpleLongProperty();

    private final SimpleLongProperty seenCount = new SimpleLongProperty();

    private final SimpleBooleanProperty includeUnknown = new SimpleBooleanProperty();

    private final ObservableList<FilterSessionWord> seenWords = FXCollections.observableArrayList();

    public FilterSessionModel(final Path file, final List<? extends MarkedWord> words) {
        super(file);
        setupValues(words);
        bindValues();
    }

    public void replaceContent(final Path file, final List<? extends MarkedWord> words) {
        replaceContent(file);
        setupValues(words);
    }

    private void setupValues(final List<? extends MarkedWord> words) {
        Map<WordState, Long> counts = words.stream()
            .collect(Collectors.groupingBy(MarkedWord::getState, Collectors.counting()));

        knownCount.set(counts.getOrDefault(WordState.KNOWN, 0L));
        seenCount.set(counts.getOrDefault(WordState.UNKNOWN, 0L) + knownCount.get());
        seenWords.clear();
        words.stream()
            .filter(w -> w.getState() != WordState.UNSEEN)
            .map(FilterSessionWord::new)
            .forEach(seenWords::add);
    }

    @Override
    protected ObservableNumberValue countValue() {
        return Bindings.when(includeUnknown).then(seenCount).otherwise(knownCount);
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
}
