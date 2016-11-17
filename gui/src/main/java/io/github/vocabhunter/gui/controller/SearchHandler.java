/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.core.GuiTaskHandler;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.CustomTextField;

import java.text.MessageFormat;

public class SearchHandler {
    private final GuiTaskHandler guiTaskHandler;

    private final SessionModel model;

    private final WordListHandler wordListHandler;

    private final ToolBar barSearch;

    private final CustomTextField fieldSearch;

    private final Label labelMatches;

    private final Button buttonCloseSearch;

    public SearchHandler(
        final GuiTaskHandler guiTaskHandler, final SessionModel model, final WordListHandler wordListHandler, final ToolBar barSearch, final CustomTextField fieldSearch,
        final Label labelMatches, final Button buttonCloseSearch) {
        this.guiTaskHandler = guiTaskHandler;
        this.model = model;
        this.wordListHandler = wordListHandler;
        this.barSearch = barSearch;
        this.fieldSearch = fieldSearch;
        this.labelMatches = labelMatches;
        this.buttonCloseSearch = buttonCloseSearch;
    }


    public void prepare() {
        BooleanBinding blankSearch = fieldSearch.textProperty().isEmpty();
        IntegerBinding matchCount = Bindings.createIntegerBinding(() -> matchCount(), fieldSearch.textProperty(), model.getWordList());
        StringBinding matchText = Bindings.createStringBinding(() -> formatMatches(matchCount, blankSearch), matchCount, blankSearch);

        labelMatches.textProperty().bind(matchText);
        fieldSearch.textProperty().addListener((o, old, v) -> processTextUpdate(v));

        buttonCloseSearch.setOnAction(e -> closeSearch());

        barSearch.visibleProperty().bindBidirectional(model.searchOpenProperty());
        barSearch.managedProperty().bindBidirectional(model.searchOpenProperty());
        barSearch.visibleProperty().addListener((o, old, v) -> focus(v));
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

    private String formatMatches(final ObservableNumberValue matchCount, final ObservableBooleanValue blankSearch) {
        if (blankSearch.get()) {
            return "";
        } else {
            return MessageFormat.format("{0} {0,choice,0#matches|1#match|1<matches}", matchCount.intValue());
        }
    }

    private int matchCount() {
        String searchText = fieldSearch.getText().trim().toLowerCase();

        if ("".equals(searchText)) {
            return 0;
        } else {
            return (int) model.getWordList().stream()
                .filter(w -> isMatch(w, searchText))
                .count();
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
