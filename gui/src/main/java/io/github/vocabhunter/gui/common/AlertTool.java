/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

import javafx.scene.control.Alert;

public final class AlertTool {
    private AlertTool() {
        // Prevent instantiation - all methods are static
    }

    public static void filterErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Filter Error");
        alert.setHeaderText("The selected filters would hide all of the words.");
        alert.setContentText("The filters have been disabled.");

        alert.showAndWait();
    }
}
