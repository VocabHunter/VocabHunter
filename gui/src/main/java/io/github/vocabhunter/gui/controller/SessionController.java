/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.core.GuiTaskHandler;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.filter.WordFilter;
import io.github.vocabhunter.analysis.marked.MarkTool;
import io.github.vocabhunter.analysis.marked.WordState;
import io.github.vocabhunter.gui.model.PositionModel;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;
import io.github.vocabhunter.gui.services.FilterService;
import io.github.vocabhunter.gui.view.UseListCell;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import org.controlsfx.control.textfield.CustomTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static io.github.vocabhunter.gui.common.EventHandlerTool.combine;
import static io.github.vocabhunter.gui.dialogues.AlertTool.filterErrorAlert;

public class SessionController {
    private static final Logger LOG = LoggerFactory.getLogger(SessionController.class);

    @FXML
    private Label mainWord;

    @FXML
    private Label useCountLabel;

    @FXML
    private ListView<WordModel> wordListView;

    @FXML
    private Pane mainWordPane;

    @FXML
    private Button buttonUnseen;

    @FXML
    private Button buttonKnown;

    @FXML
    private Button buttonUnknown;

    @FXML
    private Button buttonNote;

    @FXML
    private ListView<String> useListView;

    @FXML
    private SplitPane splitUseList;

    @FXML
    private SplitPane splitWordList;

    @FXML
    private ToolBar barSearch;

    @FXML
    private CustomTextField fieldSearch;

    @FXML
    private Label labelMatches;

    @FXML
    private Button buttonCloseSearch;

    @FXML
    private Button buttonSearchUp;

    @FXML
    private Button buttonSearchDown;

    @FXML
    private TextArea textAreaNotePreview;

    @Inject
    private FilterService filterService;

    @Inject
    private WordStateHandler wordStateHandler;

    @Inject
    private WordNoteHandler wordNoteHandler;

    private SessionModel sessionModel;

    private ObjectBinding<WordState> wordStateProperty;

    private WordListHandler wordListHandler;

    private MainWordHandler mainWordHandler;

    private SearchHandler searchHandler;

    private SessionActions sessionActions;

    public void initialise(final GuiTaskHandler guiTaskHandler, final SessionModel sessionModel) {
        this.sessionModel = sessionModel;

        wordStateProperty = Bindings.select(sessionModel.currentWordProperty(), "state");

        useListView.setItems(sessionModel.getUseList());
        useListView.setCellFactory(s -> new UseListCell(sessionModel.currentWordProperty()));
        wordListView.getSelectionModel().selectedItemProperty().addListener((o, old, word) -> updateCurrentWordProperty(word));

        prepareWordListHandler();
        wordStateHandler.initialise(buttonUnseen, buttonKnown, buttonUnknown, sessionModel, wordStateProperty, wordListHandler::selectNextWord);
        wordNoteHandler.initialise(buttonNote, textAreaNotePreview, sessionModel);

        prepareMainWord();
        prepareSearchBar(guiTaskHandler);
        preparePositionModel();
        prepareWindowBindings();

        mainWordHandler.processWordUpdate(sessionModel.getCurrentWord());

        sessionModel.editableProperty().addListener((p, o, v) -> updateWordList());
        sessionModel.filterSettingsProperty().addListener((p, o, v) -> updateWordListIfFilterEnabled());
        sessionModel.enableFiltersProperty().addListener((p, o, v) -> updateWordList());

        sessionActions = new SessionActions(combine(searchHandler::processKeyPress, wordStateHandler::processKeyPress, wordNoteHandler::processKeyPress), searchHandler::openSearch);
    }

    private void prepareWordListHandler() {
        wordListHandler = new WordListHandler(wordListView, sessionModel);
        wordListHandler.prepare();
    }

    private void prepareMainWord() {
        mainWordHandler = new MainWordHandler(mainWord, useCountLabel, mainWordPane, sessionModel, wordStateProperty);
        mainWordHandler.prepare();
    }

    private void prepareSearchBar(final GuiTaskHandler guiTaskHandler) {
        searchHandler = new SearchHandler(guiTaskHandler, sessionModel, wordListHandler, barSearch, fieldSearch, labelMatches, buttonCloseSearch, buttonSearchUp, buttonSearchDown);

        searchHandler.prepare();
    }

    private void preparePositionModel() {
        PositionModel position = sessionModel.getPosition();

        position.positionIndexProperty().bind(wordListView.getSelectionModel().selectedIndexProperty());
        position.sizeProperty().bind(Bindings.size(wordListView.getItems()));
        position.editableProperty().bind(sessionModel.editableProperty());
    }

    private void prepareWindowBindings() {
        splitUseList.getDividers().get(0).positionProperty().bindBidirectional(sessionModel.splitUsePositionProperty());
        splitWordList.getDividers().get(0).positionProperty().bindBidirectional(sessionModel.splitWordPositionProperty());
    }

    private void updateWordListIfFilterEnabled() {
        boolean isFilterEnabled = sessionModel.isEnableFilters();

        if (isFilterEnabled) {
            updateWordList();
        }
    }

    private void updateWordList() {
        boolean isEditable = sessionModel.isEditable();
        VocabHunterException exception;
        boolean isFilterSuccess;

        try {
            WordFilter filter = filterService.getFilter(sessionModel.isEnableFilters());
            MarkTool<WordModel> markTool = new MarkTool<>(filter, sessionModel.getAllWords());

            isFilterSuccess = markTool.isValidFilter();
            if (isFilterSuccess) {
                sessionModel.updateWordList(isEditable, markTool);
                wordListHandler.selectClosestWord(isEditable, filter);
            }
            exception = null;
        } catch (final VocabHunterException e) {
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

    public SessionActions getSessionActions() {
        return sessionActions;
    }
}
