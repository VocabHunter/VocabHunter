/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.vocabhunter.analysis.core.VocabHunterException;
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
import io.github.vocabhunter.gui.model.StatusModel;
import io.github.vocabhunter.gui.settings.SettingsManager;
import io.github.vocabhunter.gui.settings.WindowSettings;
import io.github.vocabhunter.gui.status.StatusActionManager;
import io.github.vocabhunter.gui.status.StatusManager;
import io.github.vocabhunter.gui.view.SessionTab;
import io.github.vocabhunter.gui.view.SessionViewTool;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.StatusBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

    public StatusBar statusBar;

    public Pane maskerPane;

    private Stage stage;

    private GuiFactory factory;

    private FileStreamer fileStreamer;

    private SettingsManager settingsManager;

    private StatusManager statusManager;

    private EventHandler<KeyEvent> keyPressHandler;

    private MainModel model;

    private StatusActionManager statusActionManager;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    public void initialise(final Stage stage, final GuiFactory factory, final FileStreamer fileStreamer, final SettingsManager settingsManager,
                           final FileListManager fileListManager, final EnvironmentManager environmentManager, final StatusManager statusManager,
                           final StatusActionManager statusActionManager, final WebPageTool webPageTool, final MainModel model) {
        this.model = model;
        this.stage = stage;
        this.factory = factory;
        this.fileStreamer = fileStreamer;
        this.settingsManager = settingsManager;
        this.statusManager = statusManager;
        this.statusActionManager = statusActionManager;

        buildToggleGroup(buttonEditOn, buttonEditOff);
        buildToggleGroup(menuEditOn, menuEditOff);

        handler(buttonOpen, menuOpen, () -> processFileWithCheck(factory::openSessionChooser, this::processOpenSession), StatusManager::beginOpenSession);
        handler(buttonNew, menuNew, () -> processFileWithCheck(factory::newSessionChooser, this::processNewSession), StatusManager::beginNewSession);
        handler(buttonSave, menuSave, this::processSave, StatusManager::beginSaveSession);
        handler(menuSaveAs, this::processSaveAs, StatusManager::beginSaveSession);
        handler(buttonExport, menuExport, () -> processFile(factory::exportSelectionChooser, this::processExport), StatusManager::beginExport);

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

        handler(buttonSetupFilters, menuSetupFilters, this::processSetupFilters);

        menuWebsite.setOnAction(e -> webPageTool.showWebPage(GuiConstants.WEBSITE));
        menuHowTo.setOnAction(e -> webPageTool.showWebPage(GuiConstants.WEBPAGE_HELP));
        menuIssue.setOnAction(e -> webPageTool.showWebPage(GuiConstants.WEBPAGE_ISSUE));
        menuAbout.setOnAction(e -> statusActionManager.wrapHandler(this::processAbout, StatusManager::beginAbout));

        factory.getExternalEventSource().setListener(e -> processOpenOrNew(e.getFile()));

        prepareTitleHandler();
        prepareFilterHandler(settingsManager, fileListManager);
        prepareStatusInformation();

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

    private void prepareStatusInformation() {
        StatusModel statusModel = model.getStatusModel();

        statusBar.textProperty().bind(statusModel.textProperty());
        statusBar.progressProperty().bind(statusModel.progressProperty());
        maskerPane.visibleProperty().bind(statusModel.busyProperty());
    }

    private void handler(final MenuItem menuItem, final Supplier<Boolean> action, final Consumer<StatusManager> beginStatus) {
        EventHandler<ActionEvent> handler = e -> statusActionManager.wrapHandler(action, beginStatus);

        menuItem.setOnAction(handler);
    }

    private void handler(final Button button, final MenuItem menuItem, final Supplier<Boolean> handler, final Consumer<StatusManager> beginStatus) {
        handler(button, menuItem, () -> statusActionManager.wrapHandler(handler, beginStatus));
    }

    private void handler(final Button button, final MenuItem menuItem, final Runnable action) {
        EventHandler<ActionEvent> handler = e -> action.run();

        button.setOnAction(handler);
        menuItem.setOnAction(handler);
    }

    private void buildToggleGroup(final Toggle editOn, final Toggle editOff) {
        ToggleGroup editGroup = new ToggleGroup();

        editOn.setToggleGroup(editGroup);
        editOff.setToggleGroup(editGroup);
    }

    private boolean processFileWithCheck(final Function<Stage, FileDialogue> chooserFactory, final Function<FileDialogue, Boolean> processor) {
        boolean isProcessRequired = unsavedChangesCheck();

        if (isProcessRequired) {
            return processFile(chooserFactory, processor);
        } else {
            return false;
        }
    }

    private void processOpenOrNew(final Path file) {
        boolean isProcessRequired = unsavedChangesCheck();

        if (isProcessRequired) {
            LOG.info("Opening file '{}'", file);
            processOpen(file, fileStreamer::createOrOpenSession);
        }
    }

    private boolean processFile(final Function<Stage, FileDialogue> chooserFactory, final Function<FileDialogue, Boolean> processor) {
        FileDialogue chooser = chooserFactory.apply(stage);

        chooser.showChooser();
        if (chooser.isFileSelected()) {
            return processor.apply(chooser);
        } else {
            return false;
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

    private boolean processOpenSession(final FileDialogue chooser) {
        statusManager.performAction();

        Path file = chooser.getSelectedFile();

        LOG.info("Opening session file '{}'", file);
        return processOpen(file, SessionSerialiser::read);
    }

    private boolean processNewSession(final FileDialogue chooser) {
        statusManager.performAction();

        Path file = chooser.getSelectedFile();

        LOG.info("New session from '{}'", file);
        return processOpen(file, fileStreamer::createNewSession);
    }

    private boolean processOpen(final Path file, final Function<Path, EnrichedSessionState> opener) {
        try {
            EnrichedSessionState enrichedState = opener.apply(file);
            SessionState state = enrichedState.getState();
            SessionModel sessionModel = addSession(state);

            model.replaceSessionModel(state, sessionModel, enrichedState.getFile().orElse(null));
            statusManager.replaceSession(sessionModel.getPosition(), sessionModel.getProgress());

            return true;
        } catch (final RuntimeException e) {
            handleFileError(file, e, "Open File Error", "Couldn't open file '%s'", "Unable to open file '{}'");

            return false;
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

            return saveFile();
        } else {
            return false;
        }
    }

    private boolean saveFile() {
        statusManager.performAction();

        Path file = model.getSessionFile();

        try {
            LOG.info("Saving file '{}'", file);
            SessionSerialiser.write(file, getSessionState());
            model.setChangesSaved(true);

            return true;
        } catch (final RuntimeException e) {
            handleFileError(file, e, "Save Session Error", "Couldn't save file '%s'", "Unable to save session file '{}'");

            return false;
        }
    }

    private boolean processExport(final FileDialogue chooser) {
        statusManager.performAction();

        Path file = chooser.getSelectedFile();
        file = FileNameTool.ensureExportFileHasSuffix(file);

        try {
            LOG.info("Exporting to file '{}'", file);
            SelectionExportTool.exportSelection(getSessionState(), file);

            return true;
        } catch (final RuntimeException e) {
            handleFileError(file, e, "Export Error", "Couldn't create export file '%s'", "Unable export to file '{}'");

            return false;
        }
    }

    private SessionState getSessionState() {
        return model.getSessionState().orElseThrow(() -> new VocabHunterException("No session state available"));
    }

    private SessionModel addSession(final SessionState state) {
        SessionViewTool viewTool = new SessionViewTool();
        SessionModelTool sessionTool = new SessionModelTool(state, model.getFilterSettings(), viewTool.selectedProperty(), settingsManager.getWindowSettings().orElseGet(WindowSettings::new));
        SessionModel sessionModel = sessionTool.buildModel();
        ControllerAndView<SessionController, Node> cav = factory.session(sessionModel);

        viewTool.setTabContent(SessionTab.ANALYSIS, cav.getView());
        viewTool.setTabContent(SessionTab.PROGRESS, factory.progress(sessionModel.getProgress()));
        mainBorderPane.setCenter(viewTool.getView());

        keyPressHandler = cav.getController().getKeyPressHandler();

        return sessionModel;
    }

    private void processSetupFilters() {
        SettingsDialogue dialogue = factory.settingsDialogue(model);

        dialogue.show();
    }

    private boolean processAbout() {
        AboutDialogue dialogue = factory.aboutDialogue();

        dialogue.show();

        return true;
    }

    public EventHandler<KeyEvent> getKeyPressHandler() {
        return this::handleKeyEvent;
    }

    public EventHandler<WindowEvent> getCloseRequestHandler() {
        return e -> statusActionManager.wrapNoWaitHandler(() -> handleExitRequest(e), StatusManager::beginExit);
    }

    private boolean handleExitRequest(final WindowEvent e) {
        boolean isContinue = unsavedChangesCheck();

        if (isContinue) {
            WindowSettings windowSettings = new WindowSettings();

            windowSettings.setX(stage.getX());
            windowSettings.setY(stage.getY());
            windowSettings.setWidth(stage.getWidth());
            windowSettings.setHeight(stage.getHeight());
            model.getSessionModel().ifPresent(s -> saveSplitPositions(windowSettings, s));

            settingsManager.setWindowSettings(windowSettings);
        } else {
            e.consume();
        }

        return isContinue;
    }

    private void saveSplitPositions(final WindowSettings windowSettings, final SessionModel sessionModel) {
        windowSettings.setSplitUsePosition(sessionModel.getSplitUsePosition());
        windowSettings.setSplitWordPosition(sessionModel.getSplitWordPosition());
    }

    private void handleKeyEvent(final KeyEvent event) {
        if (keyPressHandler != null) {
            keyPressHandler.handle(event);
        }
    }
}
