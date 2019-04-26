/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.common.ControllerAndView;
import io.github.vocabhunter.gui.dialogues.DialogueTool;
import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.model.FilterFileModel;
import io.github.vocabhunter.gui.view.FxmlHandler;
import io.github.vocabhunter.gui.view.ViewFxml;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class BaseFilterHandler<T extends AbstractFilterController<?>> {
    private final FxmlHandler fxmlHandler;

    private final DialogueTool dialogueTool;

    private final ViewFxml viewFxml;

    private final I18nKey windowTitleKey;

    protected BaseFilterHandler(final FxmlHandler fxmlHandler, final DialogueTool dialogueTool, final ViewFxml viewFxml, final I18nKey windowTitleKey) {
        this.fxmlHandler = fxmlHandler;
        this.dialogueTool = dialogueTool;
        this.viewFxml = viewFxml;
        this.windowTitleKey = windowTitleKey;
    }

    public void show(final FilterFileModel model, final Runnable onSave) {
        Stage stage = new Stage();
        ControllerAndView<T, Parent> cav = fxmlHandler.loadControllerAndView(viewFxml);
        Parent root = cav.getView();
        T controller = cav.getController();

        try {
            controller.initialise(stage, model, onSave);
            dialogueTool.setupModal(stage, root, windowTitleKey);
        } catch (final RuntimeException e) {
            dialogueTool.errorOnOpen(model.getFile(), e);
        }
    }
}
