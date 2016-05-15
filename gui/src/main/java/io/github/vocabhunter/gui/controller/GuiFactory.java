/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.gui.common.ControllerAndView;
import io.github.vocabhunter.gui.dialogues.*;
import io.github.vocabhunter.gui.event.ExternalEventSource;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.model.ProgressModel;
import io.github.vocabhunter.gui.model.SessionModel;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

public interface GuiFactory {
    ControllerAndView<MainController, Parent> mainWindow();

    ControllerAndView<SessionController, Node> session(SessionModel model);

    Node progress(ProgressModel model);

    FileDialogue newSessionChooser(Stage stage);

    FileDialogue saveSessionChooser(Stage stage);

    FileDialogue openSessionChooser(Stage stage);

    FileDialogue exportSelectionChooser(Stage stage);

    UnsavedChangesDialogue unsavedChangesDialogue(MainModel model);

    ErrorDialogue errorDialogue(String title, String message, Throwable e);

    AboutDialogue aboutDialogue();

    SettingsDialogue settingsDialogue(MainModel model);

    ExternalEventSource getExternalEventSource();
}
