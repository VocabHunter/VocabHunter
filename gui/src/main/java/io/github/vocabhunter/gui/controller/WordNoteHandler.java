/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.common.ControllerAndView;
import io.github.vocabhunter.gui.common.EventHandlerTool;
import io.github.vocabhunter.gui.dialogues.DialogueTool;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.WordModel;
import io.github.vocabhunter.gui.view.FxmlHandler;
import io.github.vocabhunter.gui.view.ViewFxml;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import javax.inject.Inject;

import static io.github.vocabhunter.gui.i18n.I18nKey.NOTE_WINDOW_TITLE;
import static javafx.beans.binding.Bindings.selectString;

public class WordNoteHandler {
    private final FxmlHandler fxmlHandler;

    private final DialogueTool dialogueTool;

    private SessionModel sessionModel;

    @Inject
    public WordNoteHandler(final FxmlHandler fxmlHandler, final DialogueTool dialogueTool) {
        this.fxmlHandler = fxmlHandler;
        this.dialogueTool = dialogueTool;
    }

    public void initialise(final Button buttonNote, final TextArea textAreaNotePreview, final SessionModel sessionModel) {
        this.sessionModel = sessionModel;

        buttonNote.setOnAction(e -> show());
        bindProperties(textAreaNotePreview, sessionModel);
    }

    private void bindProperties(final TextArea textAreaNotePreview, final SessionModel sessionModel) {
        SimpleObjectProperty<WordModel> currentWordProperty = sessionModel.currentWordProperty();
        StringBinding noteBinding = selectString(currentWordProperty, "note");

        textAreaNotePreview.textProperty().bind(noteBinding);
        textAreaNotePreview.visibleProperty().bind(noteBinding.isNotEmpty());
    }

    private void show() {
        Stage stage = new Stage();
        ControllerAndView<WordNoteController, Parent> cav = fxmlHandler.loadControllerAndView(ViewFxml.WORD_NOTE);
        Parent root = cav.getView();
        WordNoteController controller = cav.getController();

        controller.initialise(stage, sessionModel);
        dialogueTool.setupModal(stage, root, NOTE_WINDOW_TITLE);
    }

    public void processKeyPress(final KeyEvent event) {
        if (EventHandlerTool.isSimpleKeyPress(event, KeyCode.N)) {
            event.consume();
            show();
        }
    }
}
