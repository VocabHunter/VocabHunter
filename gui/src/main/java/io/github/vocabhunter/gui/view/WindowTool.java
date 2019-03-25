/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WindowTool {
    private final I18nManager i18nManager;

    @Inject
    public WindowTool(final I18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    public void setupModal(final Stage stage, final Parent root, final I18nKey titleKey) {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(i18nManager.text(titleKey));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
