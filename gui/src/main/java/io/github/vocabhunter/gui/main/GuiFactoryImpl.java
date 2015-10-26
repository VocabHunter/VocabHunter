/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.gui.controller.MainController;
import io.github.vocabhunter.gui.controller.SessionController;
import io.github.vocabhunter.gui.dialogues.*;
import io.github.vocabhunter.gui.factory.ControllerAndView;
import io.github.vocabhunter.gui.factory.FileDialogueFactory;
import io.github.vocabhunter.gui.factory.GuiFactory;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.model.SessionModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class GuiFactoryImpl implements GuiFactory {
    private static final String FXML_MAIN = "main.fxml";

    private static final String FXML_SESSION = "session.fxml";

    private final FileDialogueFactory fileDialogueFactory;

    private final Stage stage;

    public GuiFactoryImpl(final FileDialogueFactory fileDialogueFactory, final Stage stage) {
        this.fileDialogueFactory = fileDialogueFactory;
        this.stage = stage;
    }

    @Override
    public ControllerAndView<MainController, Parent> mainWindow() {
        FXMLLoader loader = loader(FXML_MAIN);
        Parent root = loadNode(loader, FXML_MAIN);
        MainController controller = loader.<MainController>getController();

        controller.initialise(stage, this);

        return new ControllerAndView<>(controller, root);
    }

    @Override
    public ControllerAndView<SessionController, Node> session(final SessionModel model) {
        FXMLLoader loader = loader(FXML_SESSION);
        Node root = loadNode(loader, FXML_SESSION);
        SessionController controller = loader.<SessionController>getController();

        controller.initialise(model);

        return new ControllerAndView<>(controller, root);
    }

    private <T> T loadNode(final FXMLLoader loader, final String fxml) {
        try {
            return loader.load();
        } catch (final IOException e) {
            throw new VocabHunterException(String.format("Unable to load FXML_SESSION '%s'", fxml), e);
        }
    }

    private FXMLLoader loader(final String fxml) {
        return new FXMLLoader(getClass().getResource("/" + fxml));
    }

    @Override
    public FileDialogue newSessionChooser() {
        return fileDialogueFactory.create(FileDialogueType.NEW_SESSION, stage);
    }

    @Override
    public FileDialogue saveSessionChooser() {
        return fileDialogueFactory.create(FileDialogueType.SAVE_SESSION, stage);
    }

    @Override
    public FileDialogue openSessionChooser() {
        return fileDialogueFactory.create(FileDialogueType.OPEN_SESSION, stage);
    }

    @Override
    public FileDialogue exportSelectionChooser() {
        return fileDialogueFactory.create(FileDialogueType.EXPORT_SELECTION, stage);
    }

    @Override
    public UnsavedChangesDialogue unsavedChangesDialogue(final MainModel model) {
        return new UnsavedChangesDialogue(Optional.ofNullable(model.getSessionFile()));
    }

    @Override
    public ErrorDialogue errorDialogue(final String title, final String message, final Throwable e) {
        return new ErrorDialogue(title, message, e);
    }

    @Override
    public AboutDialogue aboutDialogue() {
        return new AboutDialogue();
    }
}
