/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.vocabhunter.gui.common.GuiConstants;
import io.github.vocabhunter.gui.event.ExternalEventBroker;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.model.StatusModel;
import io.github.vocabhunter.gui.services.EnvironmentManager;
import io.github.vocabhunter.gui.services.WebPageTool;
import io.github.vocabhunter.gui.status.StatusManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.StatusBar;

import javax.inject.Inject;
import javax.inject.Singleton;

import static javafx.beans.binding.Bindings.not;

@Singleton
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

    public MenuItem menuFind;

    public MenuItem menuExit;

    @Inject
    private AboutHandler aboutHandler;

    @Inject
    private StatusManager statusManager;

    @Inject
    private MainModel model;

    @Inject
    private StatusModel statusModel;

    @Inject
    private EnvironmentManager environmentManager;

    @Inject
    private WebPageTool webPageTool;

    @Inject
    private SessionStateHandler sessionStateHandler;

    @Inject
    private GuiFileHandler guiFileHandler;

    @Inject
    private SettingsHandler settingsHandler;

    @Inject
    private ExternalEventBroker externalEventSource;

    @Inject
    private ExitRequestHandler exitRequestHandler;

    public void initialise(final Stage stage) {
        sessionStateHandler.initialise(mainBorderPane);

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

        externalEventSource.setListener(e -> guiFileHandler.processOpenOrNew(e.getFile()));

        prepareFilterEnable();
        prepareStatusInformation();

        menuBar.setUseSystemMenuBar(environmentManager.useSystemMenuBar());

        menuFind.setOnAction(e -> openFind());
        menuFind.disableProperty().bind(not(model.sessionOpenProperty()));

        menuExit.setOnAction(e -> stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST)));
        menuExit.setVisible(environmentManager.isExitOptionShown());
    }

    private void prepareFilterEnable() {
        buttonEnableFilters.selectedProperty().bindBidirectional(menuEnableFilters.selectedProperty());
        buttonEnableFilters.selectedProperty().bindBidirectional(model.enableFiltersProperty());
    }

    private void prepareStatusInformation() {
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
        settingsHandler.show();
    }

    public void processAbout() {
        if (statusManager.beginAbout()) {
            try {
                aboutHandler.show();
                statusManager.markSuccess();
            } finally {
                statusManager.completeAction();
            }
        }
    }

    public EventHandler<WindowEvent> getCloseRequestHandler() {
        return this::processCloseRequest;
    }

    private void processCloseRequest(final WindowEvent e) {
        if (statusManager.beginExit()) {
            try {
                if (exitRequestHandler.handleExitRequest(e)) {
                    statusManager.markSuccess();
                }
            } finally {
                statusManager.completeAction();
            }
        }
    }

    private void openFind() {
        sessionStateHandler.getSessionActions()
            .map(SessionActions::getOpenSearchAction)
            .ifPresent(Runnable::run);
    }
}
