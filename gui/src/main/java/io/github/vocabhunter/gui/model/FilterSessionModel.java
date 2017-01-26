/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.marked.WordState;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterSessionModel {
    private final long knownCount;

    private final long seenCount;

    private final SimpleBooleanProperty includeUnknown = new SimpleBooleanProperty();

    private final FilterFileModel filterFileModel;

    private final ObservableList<FilterSessionWord> seenWords = FXCollections.observableArrayList();

    public FilterSessionModel(final List<? extends MarkedWord> words, final FilterFileModel filterFileModel) {
        Map<WordState, Long> counts = words.stream()
            .collect(Collectors.groupingBy(MarkedWord::getState, Collectors.counting()));

        knownCount = counts.getOrDefault(WordState.KNOWN, 0L);
        seenCount = counts.getOrDefault(WordState.UNKNOWN, 0L) + knownCount;
        this.filterFileModel = filterFileModel;
        words.stream()
            .filter(w -> w.getState() != WordState.UNSEEN)
            .map(FilterSessionWord::new)
            .forEach(seenWords::add);
    }

    public long getKnownCount() {
        return knownCount;
    }

    public long getSeenCount() {
        return seenCount;
    }

    public FilterFileModel getFilterFileModel() {
        return filterFileModel;
    }

    public SimpleBooleanProperty includeUnknownProperty() {
        return includeUnknown;
    }

    public boolean isIncludeUnknown() {
        return includeUnknown.get();
    }

    public ObservableList<FilterSessionWord> getSeenWords() {
        return seenWords;
    }
}
