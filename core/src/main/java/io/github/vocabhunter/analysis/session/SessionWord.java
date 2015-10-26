/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.model.WordUse;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SessionWord {
    private String wordIdentifier;

    private List<String> uses;

    private WordState state = WordState.UNSEEN;

    public SessionWord() {
        // No argument constructor to allow use as standard Java Bean
    }

    public SessionWord(final WordUse use) {
        wordIdentifier = use.getWordIdentifier();
        uses = new ArrayList<>(use.getUses());
    }

    public String getWordIdentifier() {
        return wordIdentifier;
    }

    public void setWordIdentifier(final String wordIdentifier) {
        this.wordIdentifier = wordIdentifier;
    }

    public List<String> getUses() {
        return Collections.unmodifiableList(uses);
    }

    public void setUses(final List<String> uses) {
        this.uses = new ArrayList<>(uses);
    }

    public WordState getState() {
        return state;
    }

    public void setState(final WordState state) {
        this.state = state;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SessionWord that = (SessionWord) o;

        return new EqualsBuilder()
                .append(wordIdentifier, that.wordIdentifier)
                .append(uses, that.uses)
                .append(state, that.state)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(wordIdentifier)
                .append(uses)
                .append(state)
                .toHashCode();
    }
}
