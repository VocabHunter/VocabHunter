/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.gui.common.BuildInfo;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AboutDialogue {
    private final Alert alert;

    public AboutDialogue() {
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About VocabHunter");
        alert.setHeaderText(String.format("VocabHunter Version: %s", BuildInfo.version()));
        alert.setContentText("Copyright (c) 2015 Adam Carroll.\n\n"
                + "VocabHunter is Open Source Software, published under the Apache Licence, Version 2.0.\n\n"
                + "http://vocabhunter.github.io/");
    }

    public void show() {

        alert.showAndWait();
    }
}
