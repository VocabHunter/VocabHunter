/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.vocabhunter.analysis.core.GuiTaskHandler;
import io.github.vocabhunter.analysis.settings.FileListManager;
import io.github.vocabhunter.gui.common.GuiConstants;
import io.github.vocabhunter.gui.dialogues.AboutDialogue;
import io.github.vocabhunter.gui.dialogues.SettingsDialogue;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.model.StatusModel;
import io.github.vocabhunter.gui.services.EnvironmentManager;
import io.github.vocabhunter.gui.services.SessionFileService;
import io.github.vocabhunter.gui.services.WebPageTool;
import io.github.vocabhunter.gui.settings.SettingsManager;
import io.github.vocabhunter.gui.settings.WindowSettings;
import io.github.vocabhunter.gui.status.StatusManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.StatusBar;

import static javafx.beans.binding.Bindings.not;

@SuppressFBWarnings({"NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD", "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"})
public class MainController {
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

    private SettingsManager settingsManager;

    private StatusManager statusManager;

    private MainModel model;

    private SessionStateHandler sessionStateHandler;

    private GuiFileHandler guiFileHandler;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    public void initialise(final Stage stage, final GuiFactory factory, final SessionFileService sessionFileService, final SettingsManager settingsManager,
                           final FileListManager fileListManager, final EnvironmentManager environmentManager, final StatusManager statusManager,
                           final WebPageTool webPageTool, final GuiTaskHandler guiTaskHandler, final MainModel model) {
        this.model = model;
        this.stage = stage;
        this.factory = factory;
        this.settingsManager = settingsManager;
        this.statusManager = statusManager;

        sessionStateHandler = new SessionStateHandler(mainBorderPane, factory, settingsManager, model);
        guiFileHandler = new GuiFileHandler(stage, factory, sessionFileService, statusManager, model, sessionStateHandler, guiTaskHandler);

        buildToggleGroup(buttonEditOn, buttonEditOff);
        buildToggleGroup(menuEditOn, menuEditOff);

        handler(buttonOpen, menuOpen, guiFileHandler::handleOpenSession);
        handler(buttonNew, menuNew, guiFileHandler::handleNewSession);
        handler(buttonSave, menuSave, guiFileHandler::handleSave);
        handler(menuSaveAs, guiFileHandler::handleSaveAs);
        handler(buttonExport, menuExport, guiFileHandler::handleExport);

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
        menuAbout.setOnAction(e -> processAbout());

        factory.getExternalEventSource().setListener(e -> guiFileHandler.processOpenOrNew(e.getFile()));

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
        statusBar.progressProperty().bind(statusModel.activityProperty());
        statusBar.getRightItems().add(MiniGraphTool.miniGraph(statusModel));

        maskerPane.visibleProperty().bind(statusModel.busyProperty());
    }

    private void handler(final MenuItem menuItem, final Runnable action) {
        menuItem.setOnAction(e -> action.run());
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

    private void processSetupFilters() {
        SettingsDialogue dialogue = factory.settingsDialogue(model);

        dialogue.show();
    }

    public void processAbout() {
        if (statusManager.beginAbout()) {
            try {
                AboutDialogue dialogue = factory.aboutDialogue();

                dialogue.show();
                statusManager.markSuccess();
            } finally {
                statusManager.completeAction();
            }
        }
    }

    public EventHandler<KeyEvent> getKeyPressHandler() {
        return this::handleKeyEvent;
    }

    public EventHandler<WindowEvent> getCloseRequestHandler() {
        return this::processCloseRequest;
    }

    private void processCloseRequest(final WindowEvent e) {
        if (statusManager.beginExit()) {
            try {
                if (handleExitRequest(e)) {
                    statusManager.markSuccess();
                }
            } finally {
                statusManager.completeAction();
            }
        }
    }

    private boolean handleExitRequest(final WindowEvent e) {
        boolean isContinue = guiFileHandler.unsavedChangesCheck();

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
        sessionStateHandler.getKeyPressHandler().ifPresent(k -> k.handle(event));
    }
}
