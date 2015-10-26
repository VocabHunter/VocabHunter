/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorDialogue {
    private final Alert alert;

    public ErrorDialogue(final String title, final String message, final Throwable e) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);

        TextArea textArea = new TextArea(exceptionText(e));
        VBox expContent = new VBox();

        expContent.getChildren().setAll(new Label("Error details:"), textArea);
        alert.getDialogPane().setExpandableContent(expContent);
    }

    private String exceptionText(final Throwable e) {
        StringWriter writer = new StringWriter();

        e.printStackTrace(new PrintWriter(writer));

        return writer.toString();
    }

    public void showError() {
        alert.showAndWait();
    }
}
