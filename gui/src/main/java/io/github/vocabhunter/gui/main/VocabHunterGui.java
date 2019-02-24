/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.gui.common.Placement;
import io.github.vocabhunter.gui.controller.*;
import io.github.vocabhunter.gui.model.FilterSettingsTool;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.services.PlacementManager;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VocabHunterGui {
    @Inject
    private MainController mainController;

    @Inject
    private PlacementManager placementManager;

    @Inject
    private GuiFileHandler guiFileHandler;

    @Inject
    private TitleHandler titleHandler;

    @Inject
    private MainModel model;

    @Inject
    private FilterHandler filterHandler;

    @Inject
    private ExitRequestHandler exitRequestHandler;

    @Inject
    private SessionStateHandler sessionStateHandler;

    @Inject
    private FilterSettingsTool filterSettingsTool;

    public void start(final Stage stage, final BootstrapContext bootstrapContext) {
        initialise(stage);

        stage.setOnCloseRequest(mainController.getCloseRequestHandler());

        Placement placement = placementManager.getMainWindow();

        stage.setWidth(placement.getWidth());
        stage.setHeight(placement.getHeight());
        if (placement.isPositioned()) {
            stage.setX(placement.getX());
            stage.setY(placement.getY());
        }

        Scene scene = bootstrapContext.getFutureScene().join();

        scene.setOnKeyPressed(this::handleKeyEvent);
        stage.setScene(scene);

        stage.show();

        bootstrapContext.getFutureRoot().thenAccept(r -> scheduleStartupCompletion(stage, scene, r, bootstrapContext));
    }

    private void scheduleStartupCompletion(final Stage stage, final Scene scene, final Parent r, final BootstrapContext bootstrapContext) {
        Platform.runLater(() -> completeStartup(stage, scene, r, bootstrapContext));
    }

    private void completeStartup(final Stage stage, final Scene scene, final Parent r, final BootstrapContext bootstrapContext) {
        mainController.initialise(stage);
        scene.setRoot(r);

        // We delay starting the async filtering to allow the GUI to start quickly
        filterSettingsTool.beginAsyncFiltering();

        bootstrapContext.logStartup();
    }

    private void initialise(final Stage stage) {
        guiFileHandler.initialise(stage);
        exitRequestHandler.initialise(stage);
        titleHandler.initialise();
        filterHandler.initialise();

        stage.titleProperty().bind(model.titleProperty());
    }

    private void handleKeyEvent(final KeyEvent event) {
        sessionStateHandler.getSessionActions()
            .map(SessionActions::getKeyPressHandler)
            .ifPresent(k -> k.handle(event));
    }
}
