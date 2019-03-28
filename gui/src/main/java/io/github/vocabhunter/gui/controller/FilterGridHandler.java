/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.dialogues.DialogueTool;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.view.ViewFxml;
import javafx.fxml.FXMLLoader;

import javax.inject.Inject;
import javax.inject.Provider;

import static io.github.vocabhunter.gui.i18n.I18nKey.FILTER_GRID_WINDOW_TITLE;

public class FilterGridHandler extends BaseFilterHandler<FilterGridController> {
    @Inject
    public FilterGridHandler(final Provider<FXMLLoader> loaderProvider, final I18nManager i18nManager, final DialogueTool dialogueTool) {
        super(loaderProvider, i18nManager, dialogueTool, ViewFxml.FILTER_GRID, FILTER_GRID_WINDOW_TITLE);
    }
}
