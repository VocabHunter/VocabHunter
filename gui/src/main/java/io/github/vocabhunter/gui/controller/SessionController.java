/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.analysis.marked.MarkTool;
import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.gui.model.FilterSettings;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.vocabhunter.gui.dialogues.AlertTool.filterErrorAlert;
import static io.github.vocabhunter.gui.model.FilterSettingsTool.filter;

@SuppressFBWarnings({"NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD", "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"})
public class SessionController {
    private static final Logger LOG = LoggerFactory.getLogger(SessionController.class);

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

        wordStateProperty = Bindings.select(sessionModel.currentWordProperty(), "state");

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
        VocabHunterException exception;
        boolean isFilterSuccess;

        try {
            WordFilter filter = filter(filterSettings, isFilterEnabled);
            MarkTool<WordModel> markTool = new MarkTool<>(filter, sessionModel.getAllWords());

            isFilterSuccess = markTool.isValidFilter();
            if (isFilterSuccess) {
                sessionModel.updateWordList(isEditable, markTool);
                wordListHandler.selectClosestWord(isEditable, filter);
            }
            exception = null;
        } catch (VocabHunterException e) {
            LOG.error("Failed to activate filter", e);
            exception = e;
            isFilterSuccess = false;
        }

        if (!isFilterSuccess) {
            processFilterFailure(exception);
        }
    }

    private void processFilterFailure(final VocabHunterException e) {
        Platform.runLater(() -> {
            sessionModel.setEnableFilters(false);
            if (e == null) {
                filterErrorAlert();
            } else {
                filterErrorAlert(e);
            }
        });
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
