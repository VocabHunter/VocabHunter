/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.search;

import io.github.vocabhunter.gui.common.SequencedWord;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public final class SearchResult<T extends SequencedWord> {
    private final String matchDescription;

    private final T previousMatch;

    private final T nextMatch;

    private final T wrapMatch;

    private final boolean isSearchFail;

    public SearchResult() {
        this("", null, null, null, false);
    }

    public SearchResult(final String matchDescription, final T previousMatch, final T nextMatch, final T wrapMatch, final boolean isSearchFail) {
        this.matchDescription = matchDescription;
        this.previousMatch = previousMatch;
        this.nextMatch = nextMatch;
        this.wrapMatch = wrapMatch;
        this.isSearchFail = isSearchFail;
    }

    public String getMatchDescription() {
        return matchDescription;
    }

    public T getPreviousMatch() {
        return previousMatch;
    }

    public T getNextMatch() {
        return nextMatch;
    }

    public T getWrapMatch() {
        return wrapMatch;
    }

    public boolean isSearchFail() {
        return isSearchFail;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SearchResult<?> that = (SearchResult<?>) o;

        return new EqualsBuilder()
            .append(isSearchFail, that.isSearchFail)
            .append(matchDescription, that.matchDescription)
            .append(previousMatch, that.previousMatch)
            .append(wrapMatch, that.wrapMatch)
            .append(nextMatch, that.nextMatch)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(matchDescription)
            .append(previousMatch)
            .append(nextMatch)
            .append(wrapMatch)
            .append(isSearchFail)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("matchDescription", matchDescription)
            .append("previousMatch", previousMatch)
            .append("nextMatch", nextMatch)
            .append("wrapMatch", wrapMatch)
            .append("isSearchFail", isSearchFail)
            .toString();
    }
}
