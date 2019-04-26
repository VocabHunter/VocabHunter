/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.common.ControllerAndView;
import io.github.vocabhunter.gui.dialogues.DialogueTool;
import io.github.vocabhunter.gui.view.FxmlHandler;
import io.github.vocabhunter.gui.view.ViewFxml;
import javafx.scene.Parent;
import javafx.stage.Stage;

import javax.inject.Inject;

import static io.github.vocabhunter.gui.i18n.I18nKey.FILTER_WINDOW_TITLE;

public class SettingsHandler {
    private final DialogueTool dialogueTool;

    private final FxmlHandler fxmlHandler;

    @Inject
    public SettingsHandler(final DialogueTool dialogueTool, final FxmlHandler fxmlHandler) {
        this.dialogueTool = dialogueTool;
        this.fxmlHandler = fxmlHandler;
    }

    public void show() {
        Stage stage = new Stage();
        ControllerAndView<SettingsController, Parent> cav = fxmlHandler.loadControllerAndView(ViewFxml.SETTINGS);
        Parent root = cav.getView();
        SettingsController controller = cav.getController();

        controller.initialise(stage);
        dialogueTool.setupModal(stage, root, FILTER_WINDOW_TITLE);
    }
}
