/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.vocabhunter.analysis.file.FileStreamer;
import io.github.vocabhunter.analysis.file.SelectionExportTool;
import io.github.vocabhunter.analysis.session.EnrichedSessionState;
import io.github.vocabhunter.analysis.session.FileNameTool;
import io.github.vocabhunter.analysis.session.SessionSerialiser;
import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.analysis.settings.FileListManager;
import io.github.vocabhunter.gui.common.ControllerAndView;
import io.github.vocabhunter.gui.common.EnvironmentManager;
import io.github.vocabhunter.gui.common.GuiConstants;
import io.github.vocabhunter.gui.common.WebPageTool;
import io.github.vocabhunter.gui.dialogues.*;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.settings.SettingsManager;
import io.github.vocabhunter.gui.view.SessionViewTool;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;

import static javafx.beans.binding.Bindings.not;

@SuppressFBWarnings({"NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD", "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"})
public class MainController {
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    public MenuItem menuNew;

    public MenuItem menuOpen;

    public MenuItem menuSave;

    public MenuItem menuSaveAs;

    public RadioMenuItem menuEditOn;

    public RadioMenuItem menuEditOff;

    public MenuItem menuExport;

    public MenuItem menuSetupFilters;

    public CheckMenuItem menuEnableFilters;

    public MenuItem menuWebsite;

    public MenuItem menuHowTo;

    public MenuItem menuIssue;

    public MenuItem menuAbout;

    public Button buttonNew;

    public Button buttonOpen;

    public Button buttonSave;

    public Button buttonExport;

    public Button buttonSetupFilters;

    public CheckBox buttonEnableFilters;

    public RadioButton buttonEditOn;

    public RadioButton buttonEditOff;

    public BorderPane mainBorderPane;

    public MenuBar menuBar;

    private Stage stage;

    private GuiFactory factory;

    private FileStreamer fileStreamer;

    private EventHandler<KeyEvent> keyPressHandler;

    private final MainModel model = new MainModel();

    public void initialise(final Stage stage, final GuiFactory factory, final FileStreamer fileStreamer, final SettingsManager settingsManager,
                           final FileListManager fileListManager,final EnvironmentManager environmentManager, final WebPageTool webPageTool) {
        this.stage = stage;
        this.factory = factory;
        this.fileStreamer = fileStreamer;

        buildToggleGroup(buttonEditOn, buttonEditOff);
        buildToggleGroup(menuEditOn, menuEditOff);

        handler(buttonOpen, menuOpen, e -> processFileWithCheck(factory::openSessionChooser, this::processOpenSession));
        handler(buttonNew, menuNew, e -> processFileWithCheck(factory::newSessionChooser, this::processNewSession));
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

        handler(buttonSetupFilters, menuSetupFilters, e -> processSetupFilters());

        menuWebsite.setOnAction(e -> webPageTool.showWebPage(GuiConstants.WEBSITE));
        menuHowTo.setOnAction(e -> webPageTool.showWebPage(GuiConstants.WEBPAGE_HELP));
        menuIssue.setOnAction(e -> webPageTool.showWebPage(GuiConstants.WEBPAGE_ISSUE));
        menuAbout.setOnAction(e -> processAbout());

        factory.getExternalEventSource().setListener(e -> processOpenOrNew(e.getFile()));

        prepareTitleHandler();
        prepareFilterHandler(settingsManager, fileListManager);

        menuBar.setUseSystemMenuBar(environmentManager.useSystemMenuBar());
    }

    private void prepareTitleHandler() {
        TitleHandler handler = new TitleHandler(model);

        handler.prepare();
        stage.titleProperty().bind(model.titleProperty());
    }

    private void prepareFilterHandler(final SettingsManager settingsManager, final FileListManager fileListManager) {
        FilterHandler handler = new FilterHandler(model, settingsManager, fileListManager);

        handler.prepare();
        buttonEnableFilters.selectedProperty().bindBidirectional(menuEnableFilters.selectedProperty());
        buttonEnableFilters.selectedProperty().bindBidirectional(model.enableFiltersProperty());
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

    private void processFileWithCheck(final Function<Stage, FileDialogue> chooserFactory, final Consumer<FileDialogue> processor) {
        boolean isProcessRequired = unsavedChangesCheck();

        if (isProcessRequired) {
            processFile(chooserFactory, processor);
        }
    }

    private void processOpenOrNew(final Path file) {
        boolean isProcessRequired = unsavedChangesCheck();

        if (isProcessRequired) {
            LOG.info("Opening file '{}'", file);
            processOpen(file, fileStreamer::createOrOpenSession);
        }
    }

    private void processFile(final Function<Stage, FileDialogue> chooserFactory, final Consumer<FileDialogue> processor) {
        FileDialogue chooser = chooserFactory.apply(stage);

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

    private void processOpenSession(final FileDialogue chooser) {
        Path file = chooser.getSelectedFile();

        LOG.info("Opening session file '{}'", file);
        processOpen(file, SessionSerialiser::read);
    }

    private void processNewSession(final FileDialogue chooser) {
        Path file = chooser.getSelectedFile();

        LOG.info("New session from '{}'", file);
        processOpen(file, fileStreamer::createNewSession);
    }

    private void processOpen(final Path file, final Function<Path, EnrichedSessionState> opener) {
        try {
            EnrichedSessionState enrichedState = opener.apply(file);
            SessionState state = enrichedState.getState();
            SessionModel sessionModel = addSession(state);

            model.replaceSessionModel(state, sessionModel, enrichedState.getFile().orElse(null));
        } catch (final RuntimeException e) {
            handleFileError(file, e, "Open File Error", "Couldn't open file '%s'", "Unable to open file '{}'");
        }
    }

    private void handleFileError(final Path file, final RuntimeException e, final String title, final String message, final String log) {
        LOG.info(log, file, e);

        ErrorDialogue dialogue = factory.errorDialogue(title, String.format(message, file.getFileName()), e);

        dialogue.showError();
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
        FileDialogue chooser = factory.saveSessionChooser(stage);

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
        SessionModelTool sessionTool = new SessionModelTool(state, model.getFilterSettings());
        SessionModel sessionModel = sessionTool.buildModel();
        SessionViewTool viewTool = new SessionViewTool();
        ControllerAndView<SessionController, Node> cav = factory.session(sessionModel);

        viewTool.addAnalysisView(cav.getView());
        viewTool.addProgressView(factory.progress(sessionModel.getProgress()));
        mainBorderPane.setCenter(viewTool.getView());

        keyPressHandler = cav.getController().getKeyPressHandler();

        return sessionModel;
    }

    private void processSetupFilters() {
        SettingsDialogue dialogue = factory.settingsDialogue(model);

        dialogue.show();
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
