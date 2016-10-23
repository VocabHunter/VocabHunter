/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.settings.SettingsManager;
import io.github.vocabhunter.gui.settings.WindowSettings;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ExitRequestHandler {
    @Inject
    private GuiFileHandler guiFileHandler;

    @Inject
    private SettingsManager settingsManager;

    @Inject
    private MainModel model;

    private Stage stage;

    public void initialise(final Stage stage) {
        this.stage = stage;
    }

    public boolean handleExitRequest(final WindowEvent e) {
        boolean isContinue = guiFileHandler.unsavedChangesCheck();

        if (isContinue) {
            WindowSettings windowSettings = new WindowSettings();

            windowSettings.setX(stage.getX());
            windowSettings.setY(stage.getY());
            windowSettings.setWidth(stage.getWidth());
            windowSettings.setHeight(stage.getHeight());
            model.getSessionModel().ifPresent(s -> saveSplitPositions(windowSettings, s));

            settingsManager.setWindowSettings(windowSettings);
        } else {
            e.consume();
        }

        return isContinue;
    }

    private void saveSplitPositions(final WindowSettings windowSettings, final SessionModel sessionModel) {
        windowSettings.setSplitUsePosition(sessionModel.getSplitUsePosition());
        windowSettings.setSplitWordPosition(sessionModel.getSplitWordPosition());
    }
}
