/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.common.WordNoteTool;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Singleton;

import static io.github.vocabhunter.gui.i18n.I18nKey.NOTE_TITLE;

@Singleton
public class WordNoteController {
    private final I18nManager i18nManager;

    @FXML
    private Label labelTitle;

    @FXML
    private Button buttonOk;

    @FXML
    private Button buttonCancel;

    @FXML
    private TextArea textAreaNoteText;

    private Stage stage;

    private SessionModel sessionModel;

    @Inject
    public WordNoteController(final I18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    public void initialise(final Stage stage, final SessionModel sessionModel) {
        this.stage = stage;
        this.sessionModel = sessionModel;

        labelTitle.setText(titleText());
        textAreaNoteText.setText(sessionModel.getCurrentWord().getNote());

        buttonOk.setOnAction(e -> exit(true));
        buttonCancel.setOnAction(e -> exit(false));
    }

    private String titleText() {
        return i18nManager.text(NOTE_TITLE, sessionModel.getCurrentWord().getWordIdentifier());
    }

    private void exit(final boolean isSaveRequested) {
        if (isSaveRequested) {
            WordModel currentWord = sessionModel.getCurrentWord();
            WordNoteTool tool = new WordNoteTool(currentWord.getNote(), textAreaNoteText.getText());

            if (tool.isModified()) {
                currentWord.setNote(tool.getCleaned());
                sessionModel.setChangesSaved(false);
            }
        }
        stage.close();
    }
}
