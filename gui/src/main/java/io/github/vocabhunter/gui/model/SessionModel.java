/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.model;

import io.github.vocabhunter.analysis.marked.MarkTool;
import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.gui.settings.WindowSettings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public final class SessionModel {
    private static final Comparator<WordModel> WORD_COMPARATOR = Comparator.comparing(WordModel::getSequenceNo);

    private final List<String> lines;

    private final List<WordModel> allWords;

    private final ObservableSet<WordModel> selectedWords = FXCollections.observableSet(new TreeSet<>(WORD_COMPARATOR));

    private final ObservableList<WordModel> wordList =  FXCollections.observableArrayList(WordModel.PROPERTY_EXTRACTOR);

    private final ObservableList<String> useList = FXCollections.observableArrayList();

    private final SimpleObjectProperty<WordModel> currentWord;

    private final SimpleStringProperty useCount = new SimpleStringProperty();

    private final SimpleBooleanProperty editable = new SimpleBooleanProperty(true);

    private final SimpleBooleanProperty searchOpen = new SimpleBooleanProperty(false);

    private final SimpleStringProperty documentName;

    private final SimpleBooleanProperty changesSaved = new SimpleBooleanProperty(true);

    private final SimpleObjectProperty<FilterSettings> filterSettings;

    private final SimpleBooleanProperty enableFilters = new SimpleBooleanProperty();

    private final ProgressModel progress;

    private final PositionModel position;

    private final DoubleProperty splitUsePosition;

    private final DoubleProperty splitWordPosition;

    public SessionModel(
        final String documentName, final List<String> lines, final List<WordModel> words, final FilterSettings filterSettings,
        final ProgressModel progress, final PositionModel position, final WindowSettings windowSettings) {
        this.lines = new ArrayList<>(lines);
        this.documentName = new SimpleStringProperty(documentName);
        this.filterSettings = new SimpleObjectProperty<>(filterSettings);
        this.progress = progress;
        this.position = position;
        allWords = words;
        selectedWords.addAll(words.stream()
                .filter(w -> w.getState().equals(WordState.UNKNOWN))
                .collect(toList()));

        updateWordList(true, new MarkTool<>(words));
        currentWord = new SimpleObjectProperty<>(InitialSelectionTool.nextWord(allWords));

        splitUsePosition = new SimpleDoubleProperty(windowSettings.getSplitUsePosition());
        splitWordPosition = new SimpleDoubleProperty(windowSettings.getSplitWordPosition());
    }

    public void addSelectedWord(final WordModel word) {
        selectedWords.add(word);
    }

    public void removeDeselectedWord(final WordModel word) {
        selectedWords.remove(word);
    }

    public void processWordUpdate(final WordModel word) {
        List<String> uses = word.getLineNos().stream()
            .map(lines::get)
            .collect(toList());

        useList.clear();
        useList.addAll(uses);
        useCount.set(String.format("(%d uses)", word.getUseCount()));
    }

    public void updateWordList(final boolean isEditable, final MarkTool<WordModel> markTool) {
        wordList.clear();
        if (isEditable) {
            wordList.addAll(markTool.getShownWords());
            progress.updateProgress(markTool.getKnown(), markTool.getUnknown(), markTool.getUnseenUnfiltered(), markTool.getUnseenFiltered());
        } else {
            wordList.addAll(selectedWords);
        }
    }

    public ObservableSet<WordModel> getSelectedWords() {
        return selectedWords;
    }

    public boolean isSelected(final int index) {
        WordModel word = allWords.get(index);

        return selectedWords.contains(word);
    }

    public WordModel getWord(final int index) {
        return allWords.get(index);
    }

    public SimpleObjectProperty<WordModel> currentWordProperty() {
        return currentWord;
    }

    public SimpleStringProperty useCountProperty() {
        return useCount;
    }

    public SimpleBooleanProperty editableProperty() {
        return editable;
    }

    public SimpleBooleanProperty searchOpenProperty() {
        return searchOpen;
    }

    public void setSearchOpen(final boolean isSearchOpen) {
        searchOpen.set(isSearchOpen);
    }

    public boolean isSearchOpen() {
        return searchOpen.get();
    }

    public boolean isEditable() {
        return editable.get();
    }

    public WordModel getCurrentWord() {
        return currentWord.get();
    }

    public ObservableList<WordModel> getWordList() {
        return wordList;
    }

    public int getWordListSize() {
        return wordList.size();
    }

    public List<WordModel> getAllWords() {
        return unmodifiableList(allWords);
    }

    public int getAllWordsSize() {
        return allWords.size();
    }

    public ObservableList<String> getUseList() {
        return useList;
    }

    public SimpleStringProperty documentNameProperty() {
        return documentName;
    }

    public SimpleBooleanProperty changesSavedProperty() {
        return changesSaved;
    }

    public void setChangesSaved(final boolean changesSaved) {
        this.changesSaved.set(changesSaved);
    }

    public SimpleObjectProperty<FilterSettings> filterSettingsProperty() {
        return filterSettings;
    }

    public FilterSettings getFilterSettings() {
        return filterSettings.get();
    }

    public SimpleBooleanProperty enableFiltersProperty() {
        return enableFilters;
    }

    public void setEnableFilters(final boolean enableFilters) {
        this.enableFilters.set(enableFilters);
    }

    public boolean isEnableFilters() {
        return enableFilters.get();
    }

    public ProgressModel getProgress() {
        return progress;
    }

    public PositionModel getPosition() {
        return position;
    }

    public DoubleProperty splitUsePositionProperty() {
        return splitUsePosition;
    }

    public double getSplitUsePosition() {
        return splitUsePosition.get();
    }

    public DoubleProperty splitWordPositionProperty() {
        return splitWordPosition;
    }

    public double getSplitWordPosition() {
        return splitWordPosition.get();
    }
}
