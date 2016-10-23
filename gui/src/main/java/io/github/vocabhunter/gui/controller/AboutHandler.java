/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.view.ViewFxml;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class AboutHandler {
    private final Provider<FXMLLoader> loaderProvider;

    @Inject
    public AboutHandler(final Provider<FXMLLoader> loaderProvider) {
        this.loaderProvider = loaderProvider;
    }

    public void show() {
        Stage stage = new Stage();
        FXMLLoader loader = loaderProvider.get();
        Parent root = ViewFxml.ABOUT.loadNode(loader);
        AboutController controller = loader.getController();

        controller.initialise(stage);
        setupStage(stage, root);
    }

    private void setupStage(final Stage stage, final Parent root) {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}
