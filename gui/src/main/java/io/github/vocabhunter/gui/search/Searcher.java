/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.search;

import io.github.vocabhunter.gui.common.SequencedWord;
import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.github.vocabhunter.gui.i18n.I18nKey.*;

public class Searcher<T extends SequencedWord> {
    private final I18nManager i18nManager;

    private final Function<String, Predicate<SequencedWord>> matchMaker;

    public Searcher(final I18nManager i18nManager, final Function<String, Predicate<SequencedWord>> matchMaker) {
        this.i18nManager = i18nManager;
        this.matchMaker = matchMaker;
    }

    public SearchResult<T> buildResult(final List<T> wordList, final T currentWord, final String searchText) {
        if (StringUtils.isBlank(searchText)) {
            return new SearchResult<>();
        } else {
            Predicate<SequencedWord> matcher = matchMaker.apply(searchText);
            List<T> matches = wordList.stream()
                .filter(matcher)
                .toList();

            if (matches.isEmpty()) {
                return searchResult(true, null, null, null, SEARCH_MATCH_NONE);
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
        T first = matches.get(0);

        if (previousIndex == -1) {
            return searchResult(false, null, first, first, SEARCH_MATCH_SELECTION_OFF, matchCount);
        } else {
            T previous = matches.get(previousIndex);
            if (previousIndex == matchCount - 1) {
                return searchResult(false, previous, null, first, SEARCH_MATCH_SELECTION_OFF, matchCount);
            } else {
                T next = matches.get(previousIndex + 1);

                return searchResult(false, previous, next, next, SEARCH_MATCH_SELECTION_OFF, matchCount);
            }
        }
    }

    private SearchResult<T> updateForMatchWithSelection(final List<T> matches, final int matchIndex) {
        int matchCount = matches.size();
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

        return searchResult(false, previous, next, wrap, SEARCH_MATCH_SELECTION_ON, matchIndex + 1, matchCount);
    }

    private SearchResult<T> searchResult(
        final boolean isSearchFail, final T previousMatch, final T nextMatch, final T wrapMatch, final I18nKey descriptionKey, final Object... descriptionArgs) {
        String matchDescription = i18nManager.text(descriptionKey, descriptionArgs);

        return new SearchResult<>(matchDescription, previousMatch, nextMatch, wrapMatch, isSearchFail);
    }
}
