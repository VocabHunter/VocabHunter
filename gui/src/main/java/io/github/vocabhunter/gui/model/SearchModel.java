/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.gui.common.SequencedWord;
import io.github.vocabhunter.gui.search.SearchResult;
import io.github.vocabhunter.gui.search.Searcher;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.util.function.Function;
import java.util.function.Predicate;

public class SearchModel<T extends SequencedWord> {
    private final Searcher<T> searcher;

    private final StringProperty searchField;

    private final ObjectProperty<T> currentWord;

    private final ObservableList<T> wordList;

    private final StringProperty matchDescription = new SimpleStringProperty();

    private final ObjectProperty<T> previousMatch = new SimpleObjectProperty<>();

    private final ObjectProperty<T> nextMatch = new SimpleObjectProperty<>();

    private final ObjectProperty<T> wrapMatch = new SimpleObjectProperty<>();

    private final BooleanProperty previousButtonDisabled = new SimpleBooleanProperty();

    private final BooleanProperty nextButtonDisabled = new SimpleBooleanProperty();

    private final BooleanProperty searchFail = new SimpleBooleanProperty();

    public SearchModel(
        final Function<String, Predicate<SequencedWord>> matchMaker, final StringProperty searchField, final ObjectProperty<T> currentWord,
        final ObservableList<T> wordList) {
        this.searcher = new Searcher<>(matchMaker);
        this.searchField = searchField;
        this.currentWord = currentWord;
        this.wordList = wordList;
    }

    public void resetValues() {
        updateValues(new SearchResult<>());
    }

    public void updateValues() {
        SearchResult<T> result = searcher.buildResult(wordList, currentWord.get(), searchField.get());

        updateValues(result);
    }

    private void updateValues(final SearchResult<T> result) {
        matchDescription.set(result.getMatchDescription());
        previousMatch.set(result.getPreviousMatch());
        previousButtonDisabled.set(result.getPreviousMatch() == null);
        nextMatch.set(result.getNextMatch());
        wrapMatch.set(result.getWrapMatch());
        nextButtonDisabled.set(result.getNextMatch() == null);
        searchFail.set(result.isSearchFail());
    }

    public StringProperty matchDescriptionProperty() {
        return matchDescription;
    }

    public ObjectProperty<T> previousMatchProperty() {
        return previousMatch;
    }

    public ObjectProperty<T> nextMatchProperty() {
        return nextMatch;
    }

    public ObjectProperty<T> wrapMatchProperty() {
        return wrapMatch;
    }

    public BooleanProperty previousButtonDisabledProperty() {
        return previousButtonDisabled;
    }

    public BooleanProperty nextButtonDisabledProperty() {
        return nextButtonDisabled;
    }

    public BooleanProperty searchFailProperty() {
        return searchFail;
    }
}
