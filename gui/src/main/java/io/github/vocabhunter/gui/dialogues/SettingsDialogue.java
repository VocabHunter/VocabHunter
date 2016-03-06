/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.gui.model.MainModel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.BiConsumer;

public class SettingsDialogue {
    private final MainModel model;

    private final BiConsumer<MainModel, Stage> controllerInitialiser;

    private final Parent root;

    public SettingsDialogue(final MainModel model, final BiConsumer<MainModel, Stage> controllerInitialiser, final Parent root) {
        this.model = model;
        this.controllerInitialiser = controllerInitialiser;
        this.root = root;
    }

    public void show() {
        Stage stage = new Stage();

        controllerInitialiser.accept(model, stage);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Word Filter Settings");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
