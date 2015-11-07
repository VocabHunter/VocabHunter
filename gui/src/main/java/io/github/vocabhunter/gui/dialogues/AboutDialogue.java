/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.gui.controller.AboutController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AboutDialogue {
    private final AboutController controller;

    private final Parent root;

    public AboutDialogue(final AboutController controller, final Parent root) {
        this.controller = controller;
        this.root = root;
    }

    public void show() {
        Stage stage = new Stage();

        controller.initialise(stage);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
