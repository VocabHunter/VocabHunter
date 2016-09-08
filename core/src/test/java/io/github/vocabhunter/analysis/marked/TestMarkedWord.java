/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.marked;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public final class TestMarkedWord implements MarkedWord {
    private final String word;

    private final int useCount;

    private final WordState wordState;

    public TestMarkedWord(final String word, final int useCount, final WordState wordState) {
        this.word = word;
        this.useCount = useCount;
        this.wordState = wordState;
    }

    @Override
    public String getWordIdentifier() {
        return word;
    }

    @Override
    public int getUseCount() {
        return useCount;
    }

    @Override
    public WordState getState() {
        return wordState;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestMarkedWord that = (TestMarkedWord) o;

        return new EqualsBuilder()
            .append(useCount, that.useCount)
            .append(word, that.word)
            .append(wordState, that.wordState)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(word)
            .append(useCount)
            .append(wordState)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("word", word)
            .append("useCount", useCount)
            .append("wordState", wordState)
            .toString();
    }
}
