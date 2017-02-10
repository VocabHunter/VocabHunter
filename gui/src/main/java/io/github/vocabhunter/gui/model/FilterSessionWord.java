/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.marked.WordState;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FilterSessionWord {
    private final String wordIdentifier;

    private final WordState state;

    private final ObservableValue<FilterSessionWord> self = new SimpleObjectProperty<>(this);

    public FilterSessionWord(final MarkedWord word) {
        this(word.getWordIdentifier(), word.getState());
    }

    public FilterSessionWord(final String wordIdentifier, final WordState state) {
        this.wordIdentifier = wordIdentifier;
        this.state = state;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FilterSessionWord that = (FilterSessionWord) o;

        return new EqualsBuilder()
            .append(state, that.state)
            .append(wordIdentifier, that.wordIdentifier)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(state)
            .append(wordIdentifier)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("wordIdentifier", wordIdentifier)
            .append("state", state)
            .toString();
    }
}
