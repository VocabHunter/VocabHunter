/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.core.GuiTaskHandler;
import io.github.vocabhunter.gui.model.SearchModel;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.CustomTextField;

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
        SearchModel searchModel = new SearchModel(fieldSearch.textProperty(), model.currentWordProperty(), model.getWordList());

        labelMatches.textProperty().bind(searchModel.matchDescriptionProperty());
        fieldSearch.textProperty().addListener((o, old, v) -> processTextUpdate(v));

        buttonCloseSearch.setOnAction(e -> closeSearch());
        buttonSearchUp.setOnAction(e -> selectWord(searchModel.previousMatchProperty()));
        buttonSearchDown.setOnAction(e -> selectWord(searchModel.nextMatchProperty()));

        barSearch.visibleProperty().bindBidirectional(model.searchOpenProperty());
        barSearch.managedProperty().bindBidirectional(model.searchOpenProperty());
        barSearch.visibleProperty().addListener((o, old, v) -> focus(v));

        buttonSearchUp.setDisable(true);
        buttonSearchDown.setDisable(true);
        searchModel.previousButtonDisabledProperty().addListener((o, n, v) -> buttonSearchUp.setDisable(v));
        searchModel.nextButtonDisabledProperty().addListener((o, n, v) -> buttonSearchDown.setDisable(v));
    }

    private void focus(final boolean isVisible) {
        if (isVisible) {
            guiTaskHandler.pauseThenExecuteOnGuiThread(() -> fieldSearch.requestFocus());
        }
    }

    private void closeSearch() {
        fieldSearch.setText("");
        model.setSearchOpen(false);
    }

    private void selectWord(final ObjectProperty<WordModel> property) {
        WordModel word = property.get();

        if (word != null) {
            wordListHandler.selectWord(word);
        }
    }

    private void processTextUpdate(final String value) {
        if (StringUtils.isNotBlank(value)) {
            String searchText = value.trim().toLowerCase();

            model.getWordList().stream()
                .filter(w -> isMatch(w, searchText))
                .findFirst()
                .ifPresent(wordListHandler::selectWord);
        }
    }

    private boolean isMatch(final WordModel w, final String searchText) {
        return w.getWordIdentifier().toLowerCase().contains(searchText);
    }
}
