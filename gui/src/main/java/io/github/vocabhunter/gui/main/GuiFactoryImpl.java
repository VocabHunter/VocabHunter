/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.main;

import io.github.vocabhunter.analysis.core.GuiTaskHandler;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.settings.FileListManager;
import io.github.vocabhunter.gui.common.ControllerAndView;
import io.github.vocabhunter.gui.controller.*;
import io.github.vocabhunter.gui.dialogues.*;
import io.github.vocabhunter.gui.event.ExternalEventBroker;
import io.github.vocabhunter.gui.event.ExternalEventSource;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.model.ProgressModel;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.services.EnvironmentManager;
import io.github.vocabhunter.gui.services.SessionFileService;
import io.github.vocabhunter.gui.services.WebPageTool;
import io.github.vocabhunter.gui.settings.SettingsManager;
import io.github.vocabhunter.gui.status.StatusManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.picocontainer.MutablePicoContainer;

import java.io.IOException;

public class GuiFactoryImpl implements GuiFactory {
    private static final String FXML_MAIN = "main.fxml";

    private static final String FXML_SESSION = "session.fxml";

    private static final String FXML_PROGRESS = "progress.fxml";

    private static final String FXML_ABOUT = "about.fxml";

    private static final String FXML_SETTINGS = "settings.fxml";

    private final Stage stage;

    private final SettingsManager settingsManager;

    private final FileListManager fileListManager;

    private final EnvironmentManager environmentManager;

    private final StatusManager statusManager;

    private final FileDialogueFactory fileDialogueFactory;

    private final ExternalEventBroker externalEventSource;

    private final SessionFileService sessionFileService;

    private final WebPageTool webPageTool;

    private final GuiTaskHandler guiTaskHandler;

    public GuiFactoryImpl(final Stage stage, final MutablePicoContainer pico) {
        this(stage, pico.getComponent(SettingsManager.class), pico.getComponent(FileListManager.class),
             pico.getComponent(EnvironmentManager.class), pico.getComponent(StatusManager.class),
             pico.getComponent(FileDialogueFactory.class), pico.getComponent(ExternalEventBroker.class),
             pico.getComponent(SessionFileService.class), pico.getComponent(WebPageTool.class),
            pico.getComponent(GuiTaskHandler.class));
    }

    @SuppressWarnings("PMD.ExcessiveParameterList")
    private GuiFactoryImpl(final Stage stage, final SettingsManager settingsManager, final FileListManager fileListManager,
                           final EnvironmentManager environmentManager, final StatusManager statusManager,
                           final FileDialogueFactory fileDialogueFactory, final ExternalEventBroker externalEventSource,
                           final SessionFileService sessionFileService, final WebPageTool webPageTool,
                           final GuiTaskHandler guiTaskHandler) {
        this.stage = stage;
        this.settingsManager = settingsManager;
        this.fileListManager = fileListManager;
        this.environmentManager = environmentManager;
        this.statusManager = statusManager;
        this.fileDialogueFactory = fileDialogueFactory;
        this.externalEventSource = externalEventSource;
        this.sessionFileService = sessionFileService;
        this.webPageTool = webPageTool;
        this.guiTaskHandler = guiTaskHandler;
    }

    @Override
    public ControllerAndView<MainController, Parent> mainWindow() {
        MainModel model = new MainModel();
        FXMLLoader loader = loader(FXML_MAIN);
        Parent root = loadNode(loader, FXML_MAIN);
        MainController controller = loader.getController();

        statusManager.initialise(model.getStatusModel());
        controller.initialise(
            stage, this, sessionFileService, settingsManager, fileListManager, environmentManager,
            statusManager, webPageTool, guiTaskHandler, model);

        return new ControllerAndView<>(controller, root);
    }

    @Override
    public ControllerAndView<SessionController, Node> session(final SessionModel model) {
        FXMLLoader loader = loader(FXML_SESSION);
        Node root = loadNode(loader, FXML_SESSION);
        SessionController controller = loader.getController();

        controller.initialise(model);

        return new ControllerAndView<>(controller, root);
    }

    @Override
    public Node progress(final ProgressModel model) {
        FXMLLoader loader = loader(FXML_PROGRESS);
        Node root = loadNode(loader, FXML_PROGRESS);
        ProgressController controller = loader.getController();

        controller.initialise(model);

        return root;
    }

    private <T> T loadNode(final FXMLLoader loader, final String fxml) {
        try {
            return loader.load();
        } catch (final IOException e) {
            throw new VocabHunterException(String.format("Unable to load FXML_SESSION '%s'", fxml), e);
        }
    }

    @Override
    public ExternalEventSource getExternalEventSource() {
        return externalEventSource;
    }

    private FXMLLoader loader(final String fxml) {
        return new FXMLLoader(getClass().getResource("/" + fxml));
    }

    @Override
    public FileDialogue newSessionChooser(final Stage stage) {
        return fileDialogueFactory.create(FileDialogueType.NEW_SESSION, stage);
    }

    @Override
    public FileDialogue saveSessionChooser(final Stage stage) {
        return fileDialogueFactory.create(FileDialogueType.SAVE_SESSION, stage);
    }

    @Override
    public FileDialogue openSessionChooser(final Stage stage) {
        return fileDialogueFactory.create(FileDialogueType.OPEN_SESSION, stage);
    }

    @Override
    public FileDialogue exportSelectionChooser(final Stage stage) {
        return fileDialogueFactory.create(FileDialogueType.EXPORT_SELECTION, stage);
    }

    @Override
    public UnsavedChangesDialogue unsavedChangesDialogue(final MainModel model) {
        return new UnsavedChangesDialogue(model.getSessionFile());
    }

    @Override
    public AboutDialogue aboutDialogue() {
        FXMLLoader loader = loader(FXML_ABOUT);
        Parent root = loadNode(loader, FXML_ABOUT);
        AboutController controller = loader.getController();

        return new AboutDialogue(s -> controller.initialise(s, webPageTool), root);
    }

    @Override
    public SettingsDialogue settingsDialogue(final MainModel model) {
        FXMLLoader loader = loader(FXML_SETTINGS);
        Parent root = loadNode(loader, FXML_SETTINGS);
        SettingsController controller = loader.getController();

        return new SettingsDialogue(model, (m, s) -> controller.initialise(m, this, s), root);
    }
}
