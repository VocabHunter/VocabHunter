/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.core.GuiTaskHandler;
import io.github.vocabhunter.gui.common.SequencedWord;
import io.github.vocabhunter.gui.model.SearchModel;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;
import io.github.vocabhunter.gui.search.SearchTool;
import io.github.vocabhunter.gui.view.ErrorClassTool;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.CustomTextField;

import java.util.function.Predicate;

public class SearchHandler {
    private final GuiTaskHandler guiTaskHandler;

    private final SessionModel model;

    private final WordListHandler wordListHandler;

    private final ToolBar barSearch;

    private final CustomTextField fieldSearch;

    private final Label labelMatches;

    private final Button buttonCloseSearch;

    private final Button buttonSearchUp;

    private final Button buttonSearchDown;

    private SearchModel<WordModel> searchModel;

    public SearchHandler(
        final GuiTaskHandler guiTaskHandler, final SessionModel model, final WordListHandler wordListHandler, final ToolBar barSearch, final CustomTextField fieldSearch,
        final Label labelMatches, final Button buttonCloseSearch, final Button buttonSearchUp, final Button buttonSearchDown) {
        this.guiTaskHandler = guiTaskHandler;
        this.model = model;
        this.wordListHandler = wordListHandler;
        this.barSearch = barSearch;
        this.fieldSearch = fieldSearch;
        this.labelMatches = labelMatches;
        this.buttonCloseSearch = buttonCloseSearch;
        this.buttonSearchUp = buttonSearchUp;
        this.buttonSearchDown = buttonSearchDown;
    }


    public void prepare() {
        searchModel = new SearchModel<>(SearchTool::matchMaker, fieldSearch.textProperty(), model.currentWordProperty(), model.getWordList());
        labelMatches.textProperty().bind(searchModel.matchDescriptionProperty());
        fieldSearch.textProperty().addListener((o, old, v) -> processTextUpdate(v));

        buttonCloseSearch.setOnAction(e -> closeSearch());
        buttonSearchUp.setOnAction(e -> selectWord(searchModel.previousMatchProperty()));
        buttonSearchDown.setOnAction(e -> selectWord(searchModel.nextMatchProperty()));

        barSearch.visibleProperty().bindBidirectional(model.searchOpenProperty());
        barSearch.managedProperty().bindBidirectional(model.searchOpenProperty());

        buttonSearchUp.setDisable(true);
        buttonSearchDown.setDisable(true);
        searchModel.previousButtonDisabledProperty().addListener((o, n, v) -> buttonSearchUp.setDisable(v));
        searchModel.nextButtonDisabledProperty().addListener((o, n, v) -> buttonSearchDown.setDisable(v));

        searchModel.searchFailProperty().addListener((o, n, v) -> ErrorClassTool.updateClass(fieldSearch, v));

        fieldSearch.textProperty().addListener((o, n, v) -> updateIfRequired());
        fieldSearch.setOnKeyPressed(this::processSearchKeyPress);
        model.currentWordProperty().addListener((o, n, v) -> updateIfRequired());
        model.getWordList().addListener((ListChangeListener<WordModel>) c -> updateIfRequired());

        searchModel.resetValues();
    }

    private void processSearchKeyPress(final KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            selectWord(searchModel.wrapMatchProperty());
        }
    }

    private void updateIfRequired() {
        if (model.isSearchOpen()) {
            searchModel.updateValues();
        }
    }

    private void selectWord(final ObjectProperty<WordModel> property) {
        WordModel word = property.get();

        if (word != null) {
            wordListHandler.selectWord(word);
        }
    }

    private void processTextUpdate(final String value) {
        if (StringUtils.isNotBlank(value)) {
            Predicate<SequencedWord> matcher = SearchTool.matchMaker(value);

            model.getWordList().stream()
                .filter(matcher)
                .findFirst()
                .ifPresent(wordListHandler::selectWord);
        }
    }

    public void openSearch() {
        model.setSearchOpen(true);
        guiTaskHandler.pauseThenExecuteOnGuiThread(fieldSearch::requestFocus);
    }

    private void closeSearch() {
        fieldSearch.setText("");
        searchModel.resetValues();
        model.setSearchOpen(false);
    }

    public void processKeyPress(final KeyEvent event) {
        if (KeyCode.ESCAPE.equals(event.getCode())) {
            event.consume();
            closeSearch();
        }
    }
}
