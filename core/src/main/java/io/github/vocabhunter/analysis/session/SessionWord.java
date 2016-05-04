/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import io.github.vocabhunter.analysis.marked.MarkedWord;
import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.analysis.model.WordUse;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SessionWord implements MarkedWord {
    private String wordIdentifier;

    private List<String> uses;

    private int useCount;

    private WordState state = WordState.UNSEEN;

    public SessionWord() {
        // No argument constructor to allow use as standard Java Bean
    }

    public SessionWord(final WordUse use) {
        wordIdentifier = use.getWordIdentifier();
        uses = new ArrayList<>(use.getUses());
        useCount = use.getUseCount();
    }

    @Override
    public String getWordIdentifier() {
        return wordIdentifier;
    }

    public void setWordIdentifier(final String wordIdentifier) {
        this.wordIdentifier = wordIdentifier;
    }

    public List<String> getUses() {
        return Collections.unmodifiableList(uses);
    }

    @Override
    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(final int useCount) {
        this.useCount = useCount;
    }

    public void setUses(final List<String> uses) {
        this.uses = new ArrayList<>(uses);
    }

    @Override
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
            .append(useCount, that.useCount)
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
            .append(useCount)
            .append(state)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("wordIdentifier", wordIdentifier)
            .append("uses", uses)
            .append("useCount", useCount)
            .append("state", state)
            .toString();
    }
}
