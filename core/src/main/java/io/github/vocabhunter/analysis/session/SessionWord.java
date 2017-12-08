/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.session;

import com.fasterxml.jackson.annotation.JsonInclude;
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
import java.util.function.Function;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionWord implements MarkedWord {
    private String wordIdentifier;

    private List<String> uses;

    private List<Integer> lineNos;

    private int useCount;

    private WordState state = WordState.UNSEEN;

    private String note;

    public SessionWord() {
        // No argument constructor to allow use as standard Java Bean
    }

    public SessionWord(final WordUse use) {
        wordIdentifier = use.getWordIdentifier();
        lineNos = new ArrayList<>(use.getLineNos());
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
        if (uses == null) {
            return null;
        } else {
            return Collections.unmodifiableList(uses);
        }
    }

    public List<Integer> getLineNos() {
        return Collections.unmodifiableList(lineNos);
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

    public void setLineNos(final List<Integer> lineNos) {
        this.lineNos = new ArrayList<>(lineNos);
    }

    @Override
    public WordState getState() {
        return state;
    }

    public void setState(final WordState state) {
        this.state = state;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
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
        Function<SessionWord, List<?>> lineExtractor = s -> s.lineNos;

        return isEquivalent(that, lineExtractor, lineExtractor);
    }

    public boolean isEquivalent(final SessionWord that, final Function<SessionWord, List<?>> thisExtractor,  final Function<SessionWord, List<?>> thatExtractor) {
        return new EqualsBuilder()
            .append(useCount, that.useCount)
            .append(wordIdentifier, that.wordIdentifier)
            .append(uses, that.uses)
            .append(thisExtractor.apply(this), thatExtractor.apply(that))
            .append(state, that.state)
            .append(note, that.note)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(wordIdentifier)
            .append(uses)
            .append(lineNos)
            .append(useCount)
            .append(state)
            .append(note)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("wordIdentifier", wordIdentifier)
            .append("uses", uses)
            .append("lineNos", lineNos)
            .append("useCount", useCount)
            .append("state", state)
            .append("note", note)
            .toString();
    }
}
