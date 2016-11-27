/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.search;

import io.github.vocabhunter.gui.common.SequencedWord;

public final class SearchResult<T extends SequencedWord> {
    private final String matchDescription;

    private final T previousMatch;

    private final T nextMatch;

    private final boolean isSearchFail;

    public SearchResult(final String matchDescription, final T previousMatch, final T nextMatch, final boolean isSearchFail) {
        this.matchDescription = matchDescription;
        this.previousMatch = previousMatch;
        this.nextMatch = nextMatch;
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

    public boolean isSearchFail() {
        return isSearchFail;
    }
}
