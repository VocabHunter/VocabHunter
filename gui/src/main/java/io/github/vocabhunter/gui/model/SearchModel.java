/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class SearchModel {
    private final StringProperty searchField;

    private final ObjectProperty<WordModel> currentWord;

    private final ObservableList<WordModel> wordList;

    private final StringProperty matchDescription = new SimpleStringProperty();

    private final ObjectProperty<WordModel> previousMatch = new SimpleObjectProperty<>();

    private final ObjectProperty<WordModel> nextMatch = new SimpleObjectProperty<>();

    private final BooleanProperty previousButtonDisabled = new SimpleBooleanProperty();

    private final BooleanProperty nextButtonDisabled = new SimpleBooleanProperty();

    public SearchModel(final StringProperty searchField, final ObjectProperty<WordModel> currentWord, final ObservableList<WordModel> wordList) {
        this.searchField = searchField;
        this.currentWord = currentWord;
        this.wordList = wordList;

        searchField.addListener((o, n, v) -> updateValues());
        currentWord.addListener((o, n, v) -> updateValues());
        wordList.addListener((ListChangeListener<WordModel>) c -> updateValues());
        previousButtonDisabled.bind(Bindings.isNull(previousMatch));
        nextButtonDisabled.bind(Bindings.isNull(nextMatch));
    }

    private void updateValues() {
        String searchText = searchField.get().trim().toLowerCase();

        if ("".equals(searchText)) {
            matchDescription.set("");
            previousMatch.setValue(null);
            nextMatch.set(null);
        } else {
            List<WordModel> matches = wordList.stream()
                .filter(w -> isMatch(w, searchText))
                .collect(toList());

            if (matches.isEmpty()) {
                matchDescription.set("No matches");
                previousMatch.setValue(null);
                nextMatch.set(null);
            } else {
                updateForMatch(matches);
            }
        }
    }

    private void updateForMatch(final List<WordModel> matches) {
        int matchCount = matches.size();
        int matchIndex = IntStream.range(0, matchCount)
            .filter(i -> matches.get(i).equals(currentWord.get()))
            .findFirst()
            .orElse(-1);

        if (matchIndex == -1) {
            updateForMatchWithoutSelection(matches, matchCount);
        } else {
            updateForMatchWithSelection(matches, matchCount, matchIndex);
        }
    }

    private void updateForMatchWithoutSelection(final List<WordModel> matches, final int matchCount) {
        int currentSequenceNo = currentWord.get().getSequenceNo();
        int previousIndex = IntStream.range(0, matchCount)
            .map(i -> matchCount - i - 1)
            .filter(i -> matches.get(i).getSequenceNo() < currentSequenceNo)
            .findFirst()
            .orElse(-1);

        if (previousIndex == -1) {
            previousMatch.setValue(null);
            nextMatch.set(matches.get(0));
        } else {
            previousMatch.setValue(matches.get(previousIndex));
            if (previousIndex == matchCount - 1) {
                nextMatch.set(null);
            } else {
                nextMatch.set(matches.get(previousIndex + 1));
            }
        }
        matchDescription.set(MessageFormat.format("{0} {0,choice,0#matches|1#match|1<matches}", matchCount));
    }

    private void updateForMatchWithSelection(final List<WordModel> matches, final int matchCount, final int matchIndex) {
        if (matchIndex == 0) {
            previousMatch.set(null);
        } else {
            previousMatch.set(matches.get(matchIndex - 1));
        }
        if (matchIndex == matches.size() - 1) {
            nextMatch.set(null);
        } else {
            nextMatch.set(matches.get(matchIndex + 1));
        }
        matchDescription.set(MessageFormat.format("{0} of {1} {1,choice,0#matches|1#match|1<matches}", matchIndex + 1, matchCount));
    }

    private boolean isMatch(final WordModel w, final String searchText) {
        return w.getWordIdentifier().toLowerCase().contains(searchText);
    }

    public StringProperty matchDescriptionProperty() {
        return matchDescription;
    }

    public ObjectProperty<WordModel> previousMatchProperty() {
        return previousMatch;
    }

    public ObjectProperty<WordModel> nextMatchProperty() {
        return nextMatch;
    }

    public BooleanProperty previousButtonDisabledProperty() {
        return previousButtonDisabled;
    }

    public BooleanProperty nextButtonDisabledProperty() {
        return nextButtonDisabled;
    }
}
