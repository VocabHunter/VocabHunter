/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.dialogues.DialogueTool;
import io.github.vocabhunter.gui.view.FxmlHandler;
import io.github.vocabhunter.gui.view.ViewFxml;
import jakarta.inject.Inject;

import static io.github.vocabhunter.gui.i18n.I18nKey.FILTER_GRID_WINDOW_TITLE;

public class FilterGridHandler extends BaseFilterHandler<FilterGridController> {
    @Inject
    public FilterGridHandler(final FxmlHandler fxmlHandler, final DialogueTool dialogueTool) {
        super(fxmlHandler, dialogueTool, ViewFxml.FILTER_GRID, FILTER_GRID_WINDOW_TITLE);
    }
}
