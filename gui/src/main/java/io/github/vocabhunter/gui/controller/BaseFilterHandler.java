/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.dialogues.FileErrorTool;
import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.model.FilterFileModel;
import io.github.vocabhunter.gui.view.ViewFxml;
import io.github.vocabhunter.gui.view.WindowTool;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import javax.inject.Provider;

public class BaseFilterHandler<T extends AbstractFilterController<?>> {
    private final Provider<FXMLLoader> loaderProvider;

    private final I18nManager i18nManager;

    private final ViewFxml viewFxml;

    private final I18nKey windowTitleKey;

    protected BaseFilterHandler(final Provider<FXMLLoader> loaderProvider, final I18nManager i18nManager, final ViewFxml viewFxml, final I18nKey windowTitleKey) {
        this.loaderProvider = loaderProvider;
        this.i18nManager = i18nManager;
        this.viewFxml = viewFxml;
        this.windowTitleKey = windowTitleKey;
    }

    public void show(final FilterFileModel model, final Runnable onSave) {
        Stage stage = new Stage();
        FXMLLoader loader = loaderProvider.get();
        Parent root = viewFxml.loadNode(loader, i18nManager);
        T controller = loader.getController();
        String windowTitle = i18nManager.text(windowTitleKey);

        try {
            controller.initialise(stage, model, onSave);
            WindowTool.setupModal(stage, root, windowTitle);
        } catch (final RuntimeException e) {
            FileErrorTool.open(i18nManager, model.getFile(), e);
        }
    }
}
