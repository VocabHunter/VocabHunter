/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import javafx.scene.control.Alert;

public final class AlertTool {
    private static final String TITLE = "Filter Error";
    private static final String FILTER_ALL_WORDS = "The selected filters would hide all of the words.";
    private static final String FILTERS_DISABLED = "The filters have been disabled.";

    private AlertTool() {
        // Prevent instantiation - all methods are static
    }

    public static void filterErrorAlert(final Exception e) {
        ErrorDialogue dialogue = new ErrorDialogue(TITLE, e, e.getMessage(), FILTERS_DISABLED);

        dialogue.showError();
    }

    public static void filterErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(TITLE);
        alert.setHeaderText(FILTER_ALL_WORDS);
        alert.setContentText(FILTERS_DISABLED);
        alert.getDialogPane().setId("filterErrorDialogue");

        alert.showAndWait();
    }
}
