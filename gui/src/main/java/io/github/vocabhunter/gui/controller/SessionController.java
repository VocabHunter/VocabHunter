/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.analysis.session.WordState;
import io.github.vocabhunter.gui.common.AlertTool;
import io.github.vocabhunter.gui.model.FilterSettings;
import io.github.vocabhunter.gui.model.FilterTool;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;
import io.github.vocabhunter.gui.view.UseListCell;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import static io.github.vocabhunter.gui.model.FilterTool.filter;

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

        sessionModel.editableProperty().addListener((p, o, v) -> updateWordList());
        sessionModel.filterSettingsProperty().addListener((p, o, v) -> updateWordListIfFilterEnabled());
        sessionModel.enableFiltersProperty().addListener((p, o, v) -> updateWordList());
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

    private void updateWordListIfFilterEnabled() {
        boolean isFilterEnabled = sessionModel.isEnableFilters();

        if (isFilterEnabled) {
            updateWordList();
        }
    }

    private void updateWordList() {
        boolean isEditable = sessionModel.isEditable();
        FilterSettings filterSettings = sessionModel.getFilterSettings();
        boolean isFilterEnabled = sessionModel.isEnableFilters();
        WordFilter filter = filter(filterSettings, isFilterEnabled);

        if (FilterTool.isValid(filter, sessionModel.getAllWords())) {
            sessionModel.updateWordList(isEditable, filter);
            wordListHandler.selectClosestWord(isEditable, filter);
        } else {
            Platform.runLater(() -> {
                sessionModel.setEnableFilters(false);
                AlertTool.filterErrorAlert();
            });
        }
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
