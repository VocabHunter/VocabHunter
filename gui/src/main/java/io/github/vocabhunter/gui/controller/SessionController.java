/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.session.WordState;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;
import io.github.vocabhunter.gui.view.UseListCell;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class SessionController {
    public Label mainWord;

    public Label useCountLabel;

    public ListView<WordModel> wordListView;

    public BorderPane mainWordPane;

    public Button buttonUnseen;

    public Button buttonKnown;

    public Button buttonUnknown;

    public ListView<String> useListView;

    private SessionModel sessionModel;

    private ObjectBinding<WordState> wordStateProperty;

    private WordListHandler wordListHandler;

    private MainWordHandler mainWordHandler;

    private EventHandler<KeyEvent> keyPressHandler;

    public void initialise(final SessionModel sessionModel) {
        this.sessionModel = sessionModel;

        wordStateProperty = Bindings.<WordState>select(sessionModel.currentWordProperty(), "state");

        useListView.setItems(sessionModel.getUseList());
        useListView.setCellFactory(s -> new UseListCell(sessionModel.currentWordProperty()));
        wordListView.getSelectionModel().selectedItemProperty().addListener((o, old, word) -> updateCurrentWordProperty(word));

        prepareWordListHandler();
        prepareWordStateHandler();
        prepareMainWord();

        mainWordHandler.processWordUpdate(sessionModel.getCurrentWord());

        sessionModel.editableProperty().addListener((o, old, isEditable) -> updateEditState(isEditable));
    }

    private void prepareWordListHandler() {
        wordListHandler = new WordListHandler(wordListView, sessionModel);
        wordListHandler.prepare();
    }

    private void prepareWordStateHandler() {
        WordStateHandler handler = new WordStateHandler(buttonUnseen, buttonKnown, buttonUnknown, sessionModel, wordStateProperty, wordListHandler::selectNextWord);

        handler.prepareEditButtons();
        keyPressHandler = handler::processKeyPress;
    }

    private void prepareMainWord() {
        mainWordHandler = new MainWordHandler(mainWord, useCountLabel, mainWordPane, sessionModel, wordStateProperty);
        mainWordHandler.prepare();
    }

    private void updateEditState(final boolean isEditable) {
        sessionModel.updateEditState(isEditable);
        wordListHandler.selectClosestWord(isEditable);
    }

    private void updateCurrentWordProperty(final WordModel word) {
        if (word != null) {
            sessionModel.currentWordProperty().set(word);
        }
    }

    public EventHandler<KeyEvent> getKeyPressHandler() {
        return keyPressHandler;
    }

}
