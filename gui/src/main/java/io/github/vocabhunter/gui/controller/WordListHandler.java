/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;
import io.github.vocabhunter.gui.view.WordListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;

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
        wordSelectionModel.selectFirst();
    }

    public void selectNextWord() {
        wordSelectionModel.selectNext();
        int index = wordSelectionModel.getSelectedIndex();
        int scrollPosition = index - SCROLL_OFFSET;

        if (scrollPosition >= 0 && scrollPosition < sessionModel.getWordListSize()) {
            wordListView.scrollTo(scrollPosition);
        }
    }

    public void selectClosestWord(final boolean isEditable) {
        int oldIndex = sessionModel.getCurrentWord().getSequenceNo();
        WordModel closestWord = findClosestWord(oldIndex, isEditable);

        wordSelectionModel.select(closestWord);
        wordListView.scrollTo(closestWord);
    }

    private WordModel findClosestWord(final int current, final boolean isEditable) {
        if (isEditable) {
            return sessionModel.getWord(current);
        } else {
            int index = IndexTool.findClosest(current, sessionModel.getAllWordsSize(), sessionModel::isSelected);

            return sessionModel.getWord(index);
        }
    }
}
