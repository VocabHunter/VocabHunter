/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.common.Placement;
import io.github.vocabhunter.gui.model.MainModel;
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

    private final LanguageController languageController;

    private final SessionStateHandler sessionStateHandler;

    private final PlacementManager placementManager;

    private final LanguageHandler languageHandler;

    private final MainModel mainModel;

    private Stage stage;

    @Inject
    public MainStageHandler(
        final FxmlHandler fxmlHandler, final MainController mainController, final LanguageController languageController, final SessionStateHandler sessionStateHandler,
        final PlacementManager placementManager, final LanguageHandler languageHandler, final MainModel mainModel) {
        this.fxmlHandler = fxmlHandler;
        this.mainController = mainController;
        this.languageController = languageController;
        this.sessionStateHandler = sessionStateHandler;
        this.placementManager = placementManager;
        this.languageHandler = languageHandler;
        this.mainModel = mainModel;
    }

    public void initialise(final Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(mainController.getCloseRequestHandler());
        languageHandler.initialiseSceneSwitcher(this::applyNewScene);
    }

    public void applyNewScene() {
        boolean isLanguageSwitch = mainModel.getLocale() == null;
        Parent root;

        if (isLanguageSwitch) {
            root = fxmlHandler.loadNode(ViewFxml.LANGUAGE);
            languageController.initialise();
        } else {
            root = fxmlHandler.loadNode(ViewFxml.MAIN);
            mainController.initialise(stage);
        }

        Scene scene = new Scene(root);

        scene.setOnKeyPressed(this::handleKeyEvent);
        stage.setScene(scene);

        positionScene(isLanguageSwitch);
    }

    private void positionScene(final boolean isLanguageSwitch) {
        if (isLanguageSwitch) {
            stage.sizeToScene();
            stage.centerOnScreen();
        } else {
            positionPrincipalScene();
        }
    }

    private void positionPrincipalScene() {
        Placement placement = placementManager.getMainWindow();

        stage.setWidth(placement.getWidth());
        stage.setHeight(placement.getHeight());
        if (placement.isPositioned()) {
            stage.setX(placement.getX());
            stage.setY(placement.getY());
        } else {
            stage.centerOnScreen();
        }
    }

    private void handleKeyEvent(final KeyEvent event) {
        sessionStateHandler.getSessionActions()
            .map(SessionActions::getKeyPressHandler)
            .ifPresent(k -> k.handle(event));
    }
}
