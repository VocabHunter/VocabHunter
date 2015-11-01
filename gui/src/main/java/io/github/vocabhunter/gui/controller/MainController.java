/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.file.SelectionExportTool;
import io.github.vocabhunter.analysis.session.FileNameTool;
import io.github.vocabhunter.analysis.session.SessionSerialiser;
import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.gui.common.GuiConstants;
import io.github.vocabhunter.gui.dialogues.AboutDialogue;
import io.github.vocabhunter.gui.dialogues.ErrorDialogue;
import io.github.vocabhunter.gui.dialogues.FileDialogue;
import io.github.vocabhunter.gui.dialogues.UnsavedChangesDialogue;
import io.github.vocabhunter.gui.factory.ControllerAndView;
import io.github.vocabhunter.gui.factory.GuiFactory;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.model.SessionModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.URI;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static javafx.beans.binding.Bindings.not;

public class MainController {
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    public MenuItem menuNew;

    public MenuItem menuOpen;

    public MenuItem menuSave;

    public MenuItem menuSaveAs;

    public RadioMenuItem menuEditOn;

    public RadioMenuItem menuEditOff;

    public MenuItem menuExport;

    public MenuItem menuWebsite;

    public MenuItem menuHowTo;

    public MenuItem menuAbout;

    public Button buttonNew;

    public Button buttonOpen;

    public Button buttonSave;

    public Button buttonExport;

    public RadioButton buttonEditOn;

    public RadioButton buttonEditOff;

    public BorderPane mainBorderPane;

    private GuiFactory factory;

    private EventHandler<KeyEvent> keyPressHandler;

    private final MainModel model = new MainModel();

    public void initialise(final Stage stage, final GuiFactory factory) {
        this.factory = factory;

        buildToggleGroup(buttonEditOn, buttonEditOff);
        buildToggleGroup(menuEditOn, menuEditOff);

        handler(buttonOpen, menuOpen, e -> processFileWithCheck(factory::openSessionChooser, this::processOpen));
        handler(buttonNew, menuNew, e -> processFileWithCheck(factory::newSessionChooser, this::processNew));
        handler(buttonSave, menuSave, e -> processSave());
        menuSaveAs.setOnAction(e -> processSaveAs());
        handler(buttonExport, menuExport, e -> processFile(factory::exportSelectionChooser, this::processExport));

        buttonEditOn.disableProperty().bind(not(model.sessionOpenProperty()));
        menuEditOn.disableProperty().bind(not(model.sessionOpenProperty()));
        buttonEditOff.disableProperty().bind(not(model.selectionAvailableProperty()));
        menuEditOff.disableProperty().bind(not(model.selectionAvailableProperty()));

        buttonEditOn.selectedProperty().bindBidirectional(model.editModeProperty());
        menuEditOn.selectedProperty().bindBidirectional(model.editModeProperty());
        menuEditOff.selectedProperty().bindBidirectional(buttonEditOff.selectedProperty());

        buttonSave.disableProperty().bind(not(model.sessionOpenProperty()));
        menuSave.disableProperty().bind(not(model.sessionOpenProperty()));
        menuSaveAs.disableProperty().bind(not(model.sessionOpenProperty()));
        buttonExport.disableProperty().bind(not(model.selectionAvailableProperty()));
        menuExport.disableProperty().bind(not(model.selectionAvailableProperty()));

        menuWebsite.setOnAction(e -> processShowWebPage(GuiConstants.WEBSITE));
        menuHowTo.setOnAction(e -> processShowWebPage(GuiConstants.WEBPAGE_HELP));
        menuAbout.setOnAction(e -> processAbout());

        prepareTitleHandler(stage);
    }

    private void prepareTitleHandler(final Stage stage) {
        TitleHandler handler = new TitleHandler(model);

        handler.prepare();
        stage.titleProperty().bind(model.titleProperty());
    }

    private void handler(final Button button, final MenuItem menuItem, final EventHandler<ActionEvent> handler) {
        button.setOnAction(handler);
        menuItem.setOnAction(handler);
    }

    private void buildToggleGroup(final Toggle editOn, final Toggle editOff) {
        ToggleGroup editGroup = new ToggleGroup();

        editOn.setToggleGroup(editGroup);
        editOff.setToggleGroup(editGroup);
    }

    private void processFileWithCheck(final Supplier<FileDialogue> chooserFactory, final Consumer<FileDialogue> processor) {
        boolean isProcessRequired = unsavedChangesCheck();

        if (isProcessRequired) {
            processFile(chooserFactory, processor);
        }
    }

    private void processFile(final Supplier<FileDialogue> chooserFactory, final Consumer<FileDialogue> processor) {
        FileDialogue chooser = chooserFactory.get();

        chooser.showChooser();
        if (chooser.isFileSelected()) {
            processor.accept(chooser);
        }
    }

    private boolean unsavedChangesCheck() {
        if (model.isChangesSaved()) {
            return true;
        } else {
            UnsavedChangesDialogue dialogue = factory.unsavedChangesDialogue(model);

            dialogue.showDialogue();

            switch (dialogue.getUserResponse()) {
                case SAVE:
                    return processSave();
                case DISCARD:
                    return true;
                default:
                    return false;
            }
        }
    }

    private void processOpen(final FileDialogue chooser) {
        Path file = chooser.getSelectedFile();

        try {
            LOG.info("Opening file '{}'", file);
            SessionState state = SessionSerialiser.read(file);
            SessionModel sessionModel = addSession(state);

            model.replaceSessionModel(state, sessionModel, file);
        } catch (final RuntimeException e) {
            handleFileError(file, e, "Open File Error", "Couldn't open file '%s'", "Unable to open session file '{}'");
        }
    }

    private void handleFileError(final Path file, final RuntimeException e, final String title, final String message, final String log) {
        LOG.info(log, file, e);

        ErrorDialogue dialogue = factory.errorDialogue(title, String.format(message, file.getFileName()), e);

        dialogue.showError();
    }

    private void processNew(final FileDialogue chooser) {
        Path file = chooser.getSelectedFile();

        try {
            LOG.info("New session from '{}'", file);
            SessionState state = AnalysisTool.analyse(file);
            SessionModel sessionModel = addSession(state);

            model.replaceSessionModel(state, sessionModel);
        } catch (final RuntimeException e) {
            handleFileError(file, e, "New Session Error", "Couldn't open file '%s'", "Unable to open document file '{}'");
        }
    }

    private boolean processSave() {
        if (model.hasSessionFile()) {
            saveFile();

            return true;
        } else {
            return processSaveAs();
        }
    }

    private boolean processSaveAs() {
        FileDialogue chooser = factory.saveSessionChooser();

        chooser.showChooser();
        if (chooser.isFileSelected()) {
            Path file = chooser.getSelectedFile();
            file = FileNameTool.ensureSessionFileHasSuffix(file);

            model.setSessionFile(file);
            saveFile();

            return true;
        } else {
            return false;
        }
    }

    private void saveFile() {
        Path file = model.getSessionFile();

        try {
            LOG.info("Saving file '{}'", file);
            SessionSerialiser.write(file, model.getSessionState());
            model.setChangesSaved(true);
        } catch (final RuntimeException e) {
            handleFileError(file, e, "Save Session Error", "Couldn't save file '%s'", "Unable to save session file '{}'");
        }
    }

    private void processExport(final FileDialogue chooser) {
        Path file = chooser.getSelectedFile();
        file = FileNameTool.ensureExportFileHasSuffix(file);

        try {
            LOG.info("Exporting to file '{}'", file);
            SelectionExportTool.exportSelection(model.getSessionState(), file);
        } catch (final RuntimeException e) {
            handleFileError(file, e, "Export Error", "Couldn't create export file '%s'", "Unable export to file '{}'");
        }
    }

    private SessionModel addSession(final SessionState state) {
        SessionModelTool sessionTool = new SessionModelTool(state);
        SessionModel sessionModel = sessionTool.buildModel();
        ControllerAndView<SessionController, Node> cav = factory.session(sessionModel);
        SessionController controller = cav.getController();
        mainBorderPane.setCenter(cav.getView());

        keyPressHandler = controller.getKeyPressHandler();

        return sessionModel;
    }

    private void processShowWebPage(final String page) {
        try {
            Desktop.getDesktop().browse(new URI(page));
        } catch (final Exception e) {
            throw new VocabHunterException(String.format("Unable to open page %s", page), e);
        }
    }

    private void processAbout() {
        AboutDialogue dialogue = factory.aboutDialogue();

        dialogue.show();
    }

    public EventHandler<KeyEvent> getKeyPressHandler() {
        return this::handleKeyEvent;
    }

    public EventHandler<WindowEvent> getCloseRequestHandler() {
        return e -> {
            boolean isContinue = unsavedChangesCheck();

            if (!isContinue) {
                e.consume();
            }
        };
    }

    private void handleKeyEvent(final KeyEvent event) {
        if (keyPressHandler != null) {
            keyPressHandler.handle(event);
        }
    }
}
