/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.dialogues;

import io.github.vocabhunter.analysis.session.FileNameTool;
import io.github.vocabhunter.gui.common.UnsavedResponse;
import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class UnsavedChangesDialogue {
    private static final String ALERT_ID = "unsavedChanges";

    private final Path file;

    private final I18nManager i18nManager;

    private final Map<ButtonType, UnsavedResponse> map = unsavedResponseMap();

    private UnsavedResponse result;

    public UnsavedChangesDialogue(final Path file, final I18nManager i18nManager) {
        this.file = file;
        this.i18nManager = i18nManager;
    }

    public void showDialogue() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        String message = i18nManager.text(I18nKey.FILE_MODIFIED, filename());

        alert.setTitle(i18nManager.text(I18nKey.FILE_UNSAVED));
        alert.setHeaderText(message);
        alert.getButtonTypes().setAll(map.keySet());
        alert.getDialogPane().setId(ALERT_ID);

        result = alert.showAndWait()
            .map(map::get)
            .orElse(UnsavedResponse.CANCEL);
    }

    private String filename() {
        if (file == null) {
            return i18nManager.text(I18nKey.MAIN_WINDOW_UNTITLED);
        } else {
            return FileNameTool.filename(file);
        }
    }

    public UnsavedResponse getUserResponse() {
        return result;
    }

    private static Map<ButtonType, UnsavedResponse> unsavedResponseMap() {
        Map<ButtonType, UnsavedResponse> map = new LinkedHashMap<>();

        map.put(new ButtonType("Save"), UnsavedResponse.SAVE);
        map.put(new ButtonType("Discard"), UnsavedResponse.DISCARD);
        map.put(new ButtonType("Cancel"), UnsavedResponse.CANCEL);

        return map;
    }
}
