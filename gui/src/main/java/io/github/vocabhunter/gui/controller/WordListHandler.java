/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.gui.common.IndexTool;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;
import io.github.vocabhunter.gui.view.WordListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;

import java.util.function.IntPredicate;

import static io.github.vocabhunter.analysis.marked.MarkTool.isShown;

public class WordListHandler {
    private static final int SCROLL_OFFSET = 4;

    private final ListView<WordModel> wordListView;

    private final SessionModel sessionModel;

    private final MultipleSelectionModel<WordModel> wordSelectionModel;

    public WordListHandler(final ListView<WordModel> wordListView, final SessionModel sessionModel) {
        this.wordListView = wordListView;
        this.sessionModel = sessionModel;

        wordSelectionModel = wordListView.getSelectionModel();
    }

    public void prepare() {
        wordListView.setItems(sessionModel.getWordList());
        wordListView.setCellFactory(p -> new WordListCell());
        selectWord(sessionModel.getCurrentWord());
    }

    public void selectNextWord() {
        wordSelectionModel.selectNext();
        int index = wordSelectionModel.getSelectedIndex();
        int scrollPosition = index - SCROLL_OFFSET;

        if (scrollPosition >= 0 && scrollPosition < sessionModel.getWordListSize()) {
            wordListView.scrollTo(scrollPosition);
        }
    }

    public void selectClosestWord(final boolean isEditable, final WordFilter filter) {
        int oldIndex = sessionModel.getCurrentWord().getSequenceNo();
        WordModel closestWord = findClosestWord(oldIndex, isEditable, filter);

        selectWord(closestWord);
    }

    public void selectWord(final WordModel word) {
        wordSelectionModel.select(word);
        wordListView.scrollTo(word);
    }

    private WordModel findClosestWord(final int current, final boolean isEditable, final WordFilter filter) {
        IntPredicate test;
        if (isEditable) {
            test = i -> isShown(filter, sessionModel.getWord(i));
        } else {
            test = sessionModel::isSelected;
        }
        int index = IndexTool.findClosest(current, sessionModel.getAllWordsSize(), test);

        return sessionModel.getWord(index);
    }
}
