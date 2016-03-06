/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.function.Consumer;

public class AboutDialogue {
    private final Consumer<Stage> controllerInitialiser;

    private final Parent root;

    public AboutDialogue(final Consumer<Stage> controllerInitialiser, final Parent root) {
        this.controllerInitialiser = controllerInitialiser;
        this.root = root;
    }

    public void show() {
        Stage stage = new Stage();

        controllerInitialiser.accept(stage);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
