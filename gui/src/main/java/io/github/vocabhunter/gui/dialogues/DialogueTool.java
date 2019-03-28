/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import javax.inject.Inject;
import javax.inject.Singleton;

import static io.github.vocabhunter.gui.i18n.I18nKey.*;

@Singleton
public class DialogueTool {
    private static final Logger LOG = LoggerFactory.getLogger(DialogueTool.class);

    private final I18nManager i18nManager;

    @Inject
    public DialogueTool(final I18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    public void setupModal(final Stage stage, final Parent root, final I18nKey titleKey) {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(i18nManager.text(titleKey));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void errorOnOpen(final Path file, final RuntimeException e) {
        handleFileError(file, e, ERROR_SESSION_OPEN_TITLE, ERROR_SESSION_OPEN_DETAILS, "Unable to open file '{}'");
    }

    public void errorOnSave(final Path file, final RuntimeException e) {
        handleFileError(file, e, ERROR_SESSION_SAVE_TITLE, ERROR_SESSION_SAVE_DETAILS, "Unable to save session file '{}'");
    }

    public void errorOnExport(final Path file, final RuntimeException e) {
        handleFileError(file, e, ERROR_SESSION_EXPORT_TITLE, ERROR_SESSION_EXPORT_DETAILS, "Unable export to file '{}'");
    }

    private void handleFileError(final Path file, final RuntimeException e, final I18nKey titleKey, final I18nKey detailKey, final String log) {
        LOG.info(log, file, e);

        String message = i18nManager.text(detailKey, file.getFileName());
        ErrorDialogue dialogue = new ErrorDialogue(i18nManager, titleKey, e, message);

        dialogue.showError();
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
