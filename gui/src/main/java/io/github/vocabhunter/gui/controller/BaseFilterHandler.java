/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.dialogues.FileErrorTool;
import io.github.vocabhunter.gui.model.FilterFileModel;
import io.github.vocabhunter.gui.view.ViewFxml;
import io.github.vocabhunter.gui.view.WindowTool;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import javax.inject.Provider;

public class BaseFilterHandler<T extends AbstractFilterController<?>> {
    private final Provider<FXMLLoader> loaderProvider;

    private final ViewFxml viewFxml;

    private final String windowTitle;

    protected BaseFilterHandler(final Provider<FXMLLoader> loaderProvider, final ViewFxml viewFxml, final String windowTitle) {
        this.loaderProvider = loaderProvider;
        this.viewFxml = viewFxml;
        this.windowTitle = windowTitle;
    }

    public void show(final FilterFileModel model, final Runnable onSave) {
        Stage stage = new Stage();
        FXMLLoader loader = loaderProvider.get();
        Parent root = viewFxml.loadNode(loader);
        T controller = loader.getController();

        try {
            controller.initialise(stage, model, onSave);
            WindowTool.setupModal(stage, root, windowTitle);
        } catch (final RuntimeException e) {
            FileErrorTool.open(model.getFile(), e);
        }
    }
}
