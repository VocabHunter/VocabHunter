/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.gui.i18n.I18nManager;
import javafx.scene.control.Alert;

import javax.inject.Inject;
import javax.inject.Singleton;

import static io.github.vocabhunter.gui.i18n.I18nKey.*;

@Singleton
public class AlertTool {
    private final I18nManager i18nManager;

    @Inject
    public AlertTool(final I18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    public void filterErrorAlert(final Exception e) {
        ErrorDialogue dialogue = new ErrorDialogue(i18nManager, FILTER_ERROR_TITLE, e, e.getMessage(), i18nManager.text(FILTER_ERROR_DISABLED));

        dialogue.showError();
    }

    public void filterErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(i18nManager.text(FILTER_ERROR_TITLE));
        alert.setHeaderText(i18nManager.text(FILTER_ERROR_ALL));
        alert.setContentText(i18nManager.text(FILTER_ERROR_DISABLED));
        alert.getDialogPane().setId("filterErrorDialogue");

        alert.showAndWait();
    }
}
