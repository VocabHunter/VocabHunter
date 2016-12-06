/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.search;

import io.github.vocabhunter.gui.common.SequencedWord;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class Searcher<T extends SequencedWord> {
    private final Function<String, Predicate<SequencedWord>> matchMaker;

    public Searcher(final Function<String, Predicate<SequencedWord>> matchMaker) {
        this.matchMaker = matchMaker;
    }

    public SearchResult<T> buildResult(final List<T> wordList, final T currentWord, final String searchText) {
        if (StringUtils.isBlank(searchText)) {
            return new SearchResult<>();
        } else {
            Predicate<SequencedWord> matcher = matchMaker.apply(searchText);
            List<T> matches = wordList.stream()
                .filter(matcher)
                .collect(toList());

            if (matches.isEmpty()) {
                return new SearchResult<>("No matches", null, null, null, true);
            } else {
                return updateForMatch(matches, currentWord);
            }
        }
    }

    private SearchResult<T> updateForMatch(final List<T> matches, final T currentWord) {
        int matchIndex = SearchTool.getMatchIndex(matches, currentWord);

        if (matchIndex == -1) {
            return updateForMatchWithoutSelection(matches, currentWord);
        } else {
            return updateForMatchWithSelection(matches, matchIndex);
        }
    }

    private SearchResult<T> updateForMatchWithoutSelection(final List<T> matches, final T currentWord) {
        int matchCount = matches.size();
        int previousIndex = SearchTool.getPreviousMatchIndex(matches, currentWord);
        String description = MessageFormat.format("{0} {0,choice,0#matches|1#match|1<matches}", matchCount);
        T first = matches.get(0);

        if (previousIndex == -1) {
            return new SearchResult<>(description, null, first, first, false);
        } else {
            T previous = matches.get(previousIndex);
            if (previousIndex == matchCount - 1) {
                return new SearchResult<>(description, previous, null, first, false);
            } else {
                T next = matches.get(previousIndex + 1);

                return new SearchResult<>(description, previous, next, next, false);
            }
        }
    }

    private SearchResult<T> updateForMatchWithSelection(final List<T> matches, final int matchIndex) {
        int matchCount = matches.size();
        String description = MessageFormat.format("{0} of {1} {1,choice,0#matches|1#match|1<matches}", matchIndex + 1, matchCount);
        T previous;
        T next;
        T wrap;

        if (matchIndex == 0) {
            previous = null;
        } else {
            previous = matches.get(matchIndex - 1);
        }
        if (matchIndex == matchCount - 1) {
            next = null;
            wrap = matches.get(0);
        } else {
            next = matches.get(matchIndex + 1);
            wrap = next;
        }

        return new SearchResult<>(description, previous, next, wrap, false);
    }
}
