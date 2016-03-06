/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.gui.common.GuiConstants;
import io.github.vocabhunter.gui.common.UnsavedResponse;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class UnsavedChangesDialogue {
    private final Optional<Path> file;

    private final Map<ButtonType, UnsavedResponse> map = unsavedResponseMap();

    private Optional<ButtonType> result;

    public UnsavedChangesDialogue(final Optional<Path> file) {
        this.file = file;
    }

    public void showDialogue() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        String filename = file.map(Path::getFileName)
                .map(Path::toString)
                .orElse(GuiConstants.UNTITLED);
        String message = String.format("'%s' has been modified.  Do you want to save your changes?", filename);

        alert.setTitle("Unsaved Changes");
        alert.setHeaderText(message);
        alert.getButtonTypes().setAll(map.keySet());

        result = alert.showAndWait();
    }

    public UnsavedResponse getUserResponse() {
        return result.map(map::get)
                .orElse(UnsavedResponse.CANCEL);
    }

    private Map<ButtonType, UnsavedResponse> unsavedResponseMap() {
        Map<ButtonType, UnsavedResponse> map = new LinkedHashMap<>();

        map.put(new ButtonType("Save"), UnsavedResponse.SAVE);
        map.put(new ButtonType("Discard"), UnsavedResponse.DISCARD);
        map.put(new ButtonType("Cancel"), UnsavedResponse.CANCEL);

        return map;
    }
}
