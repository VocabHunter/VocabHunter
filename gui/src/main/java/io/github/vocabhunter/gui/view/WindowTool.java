/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class WindowTool {
    private WindowTool() {
        // Prevent instantiation - all methods are static
    }

    public static void setupModal(final Stage stage, final Parent root, final String title) {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
