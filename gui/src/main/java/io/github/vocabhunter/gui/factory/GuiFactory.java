/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.factory;

import io.github.vocabhunter.gui.controller.MainController;
import io.github.vocabhunter.gui.controller.SessionController;
import io.github.vocabhunter.gui.dialogues.*;
import io.github.vocabhunter.gui.event.ExternalEventSource;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.model.SessionModel;
import javafx.scene.Node;
import javafx.scene.Parent;

public interface GuiFactory {
    ControllerAndView<MainController, Parent> mainWindow();

    ControllerAndView<SessionController, Node> session(SessionModel model);

    FileDialogue newSessionChooser();

    FileDialogue saveSessionChooser();

    FileDialogue openSessionChooser();

    FileDialogue exportSelectionChooser();

    UnsavedChangesDialogue unsavedChangesDialogue(MainModel model);

    ErrorDialogue errorDialogue(String title, String message, Throwable e);

    AboutDialogue aboutDialogue();

    SettingsDialogue settingsDialogue();

    ExternalEventSource getExternalEventSource();
}
