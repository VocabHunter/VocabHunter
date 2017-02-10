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

import javax.inject.Inject;
import javax.inject.Provider;

public class FilterSessionHandler {
    @Inject
    private Provider<FXMLLoader> loaderProvider;

    public void show(final FilterFileModel model, final Runnable onSave) {
        Stage stage = new Stage();
        FXMLLoader loader = loaderProvider.get();
        Parent root = ViewFxml.FILTER_SESSION.loadNode(loader);
        FilterSessionController controller = loader.getController();

        try {
            controller.initialise(stage, model, onSave);
            WindowTool.setupModal(stage, root, "Filter Session");
        } catch (final RuntimeException e) {
            FileErrorTool.open(model.getFile(), e);
        }
    }
}
