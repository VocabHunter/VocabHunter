/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.vocabhunter.gui.model.FilterFileMode;
import io.github.vocabhunter.gui.model.FilterFileModel;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

@SuppressFBWarnings({"NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD", "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"})
public class FilterSessionController {
    public TextField fieldFile;

    public RadioButton buttonKnown;

    public RadioButton buttonSeen;

    public Button buttonAddFilterFile;

    public Button buttonCancel;

    private Stage stage;

    private FilterFileModel model;

    private Runnable onSave;

    public void initialise(final Stage stage, final FilterFileModel model, final Runnable onSave) {
        this.stage = stage;
        this.model = model;
        this.onSave = onSave;

        buildToggleGroup();
        fieldFile.setText(model.getName());

        buttonAddFilterFile.setOnAction(e -> exit(true));
        buttonCancel.setOnAction(e -> exit(false));
    }

    private void exit(final boolean isSaveRequested) {
        if (isSaveRequested) {
            FilterFileMode mode = FilterFileMode.getMode(buttonSeen.isSelected());

            model.setMode(mode);
            onSave.run();
        }
        stage.close();
    }

    private void buildToggleGroup() {
        ToggleGroup toggleGroup = new ToggleGroup();

        buttonKnown.setToggleGroup(toggleGroup);
        buttonSeen.setToggleGroup(toggleGroup);

        boolean isIncludeUnknown = model.getMode().isIncludeUnknown();

        buttonKnown.setSelected(!isIncludeUnknown);
        buttonSeen.setSelected(isIncludeUnknown);
    }
}
