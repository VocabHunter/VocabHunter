/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.common.Placement;
import io.github.vocabhunter.gui.services.PlacementManager;
import io.github.vocabhunter.gui.view.FxmlHandler;
import io.github.vocabhunter.gui.view.ViewFxml;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MainStageHandler {
    private final FxmlHandler fxmlHandler;

    private final MainController mainController;

    private final SessionStateHandler sessionStateHandler;

    private final PlacementManager placementManager;

    private Stage stage;

    @Inject
    public MainStageHandler(final FxmlHandler fxmlHandler, final MainController mainController, final SessionStateHandler sessionStateHandler, final PlacementManager placementManager) {
        this.fxmlHandler = fxmlHandler;
        this.mainController = mainController;
        this.sessionStateHandler = sessionStateHandler;
        this.placementManager = placementManager;
    }

    public void initialise(final Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(mainController.getCloseRequestHandler());
        Placement placement = placementManager.getMainWindow();

        stage.setWidth(placement.getWidth());
        stage.setHeight(placement.getHeight());
        if (placement.isPositioned()) {
            stage.setX(placement.getX());
            stage.setY(placement.getY());
        }
    }

    public void applyNewScene() {
        Parent root = fxmlHandler.loadNode(ViewFxml.MAIN);
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(this::handleKeyEvent);
        stage.setScene(scene);
        mainController.initialise(stage);
    }

    private void handleKeyEvent(final KeyEvent event) {
        sessionStateHandler.getSessionActions()
            .map(SessionActions::getKeyPressHandler)
            .ifPresent(k -> k.handle(event));
    }
}
