/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.view.ViewFxml;
import io.github.vocabhunter.gui.view.WindowTool;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Provider;

import static io.github.vocabhunter.gui.i18n.I18nKey.FILTER_WINDOW_TITLE;

public class SettingsHandler {
    @Inject
    private Provider<FXMLLoader> loaderProvider;

    @Inject
    private I18nManager i18nManager;

    @Inject
    private WindowTool windowTool;

    public void show() {
        Stage stage = new Stage();
        FXMLLoader loader = loaderProvider.get();
        Parent root = ViewFxml.SETTINGS.loadNode(loader, i18nManager);
        SettingsController controller = loader.getController();

        controller.initialise(stage);
        windowTool.setupModal(stage, root, FILTER_WINDOW_TITLE);
    }
}
