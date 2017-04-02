/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.view.ViewFxml;
import io.github.vocabhunter.gui.view.WindowTool;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Provider;

public class SettingsHandler {
    @Inject
    private Provider<FXMLLoader> loaderProvider;

    public void show() {
        Stage stage = new Stage();
        FXMLLoader loader = loaderProvider.get();
        Parent root = ViewFxml.SETTINGS.loadNode(loader);
        SettingsController controller = loader.getController();

        controller.initialise(stage);
        WindowTool.setupModal(stage, root, "Word Filter Settings");
    }
}
