/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

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

    public void show(final FilterFileModel model) {
        show(model, FilterSessionHandler::noOperation);
    }

    public void show(final FilterFileModel model, final Runnable onSave) {
        Stage stage = new Stage();
        FXMLLoader loader = loaderProvider.get();
        Parent root = ViewFxml.FILTER_SESSION.loadNode(loader);
        FilterSessionController controller = loader.getController();

        controller.initialise(stage, model, onSave);
        WindowTool.setupModal(stage, root, "Filter Session");
    }

    private static void noOperation() {
        // No operation required - used where operation is required for save
    }
}
