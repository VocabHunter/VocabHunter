/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.marked.WordState;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

public class FilterSessionWord {
    private final WordState state;

    private final String wordIdentifier;

    private final ObservableValue<FilterSessionWord> self = new SimpleObjectProperty<>(this);

    public FilterSessionWord(final MarkedWord word) {
        this.state = word.getState();
        this.wordIdentifier = word.getWordIdentifier();
    }

    public WordState getState() {
        return state;
    }

    public String getWordIdentifier() {
        return wordIdentifier;
    }

    public ObservableValue<FilterSessionWord> selfProperty() {
        return self;
    }
}
