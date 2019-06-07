/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.common.EventHandlerTool;
import io.github.vocabhunter.gui.common.GuiTaskHandler;
import io.github.vocabhunter.gui.common.SequencedWord;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.model.SearchModel;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;
import io.github.vocabhunter.gui.search.SearchTool;
import io.github.vocabhunter.gui.search.Searcher;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

public class SearchHandler {
    private final GuiTaskHandler guiTaskHandler;

    private final SessionModel model;

    private final WordListHandler wordListHandler;

    private final SearchControls controls;

    private SearchModel<WordModel> searchModel;

    private final Searcher<WordModel> searcher;

    public SearchHandler(
        final GuiTaskHandler guiTaskHandler, final I18nManager i18nManager, final WordListHandler wordListHandler, final SearchControls controls, final SessionModel model) {
        this.guiTaskHandler = guiTaskHandler;
        this.controls = controls;
        this.model = model;
        this.wordListHandler = wordListHandler;
        this.searcher = new Searcher<>(i18nManager, SearchTool::matchMaker);
    }


    public void prepare() {
        searchModel = new SearchModel<>(controls.searchFieldTextProperty(), model.currentWordProperty(), model.getWordList());
        controls.bindMatchText(searchModel.matchDescriptionProperty());
        controls.searchFieldTextProperty().addListener((o, old, v) -> processTextUpdate(v));
        controls.setupButtons(
            e -> closeSearch(),
            e -> selectWord(searchModel.previousMatchProperty()),
            e -> selectWord(searchModel.nextMatchProperty()));
        controls.bindSearchOpenProperty(model.searchOpenProperty());

        searchModel.previousButtonDisabledProperty().addListener((o, n, v) -> controls.setButtonUpDisabled(v));
        searchModel.nextButtonDisabledProperty().addListener((o, n, v) -> controls.setButtonDownDisabled(v));

        searchModel.searchFailProperty().addListener((o, n, v) -> controls.setSearchFailStatus(v));

        controls.searchFieldTextProperty().addListener((o, n, v) -> updateIfRequired());
        controls.setKeyPressHandler(this::processSearchKeyPress);
        model.currentWordProperty().addListener((o, n, v) -> updateIfRequired());
        model.getWordList().addListener((ListChangeListener<WordModel>) c -> updateIfRequired());

        searchModel.resetValues();
    }

    private void processSearchKeyPress(final KeyEvent event) {
        if (EventHandlerTool.isSimpleKeyPress(event, KeyCode.ENTER)) {
            selectWord(searchModel.wrapMatchProperty());
        }
    }

    private void updateIfRequired() {
        if (model.isSearchOpen()) {
            searchModel.updateValues(searcher);
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
        guiTaskHandler.pauseThenExecuteOnGuiThread(controls::selectSearchField);
    }

    private void closeSearch() {
        controls.clearSearchField();
        searchModel.resetValues();
        model.setSearchOpen(false);
    }

    public void processKeyPress(final KeyEvent event) {
        if (EventHandlerTool.isSimpleKeyPress(event, KeyCode.ESCAPE)) {
            event.consume();
            closeSearch();
        }
    }
}
