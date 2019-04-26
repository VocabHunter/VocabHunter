/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.common.ControllerAndView;
import io.github.vocabhunter.gui.view.FxmlHandler;
import io.github.vocabhunter.gui.view.ViewFxml;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AboutHandler {
    private final FxmlHandler fxmlHandler;

    @Inject
    public AboutHandler(final FxmlHandler fxmlHandler) {
        this.fxmlHandler = fxmlHandler;
    }

    public void show() {
        Stage stage = new Stage();
        ControllerAndView<AboutController, Parent> cav = fxmlHandler.loadControllerAndView(ViewFxml.ABOUT);
        Parent root = cav.getView();
        AboutController controller = cav.getController();

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
