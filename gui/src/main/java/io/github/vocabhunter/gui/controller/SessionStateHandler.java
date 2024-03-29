/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.gui.common.ControllerAndView;
import io.github.vocabhunter.gui.common.GuiTaskHandler;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.settings.SettingsManager;
import io.github.vocabhunter.gui.settings.WindowSettings;
import io.github.vocabhunter.gui.view.FxmlHandler;
import io.github.vocabhunter.gui.view.SessionTab;
import io.github.vocabhunter.gui.view.SessionViewTool;
import io.github.vocabhunter.gui.view.ViewFxml;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.util.Optional;

@Singleton
public class SessionStateHandler {
    @Inject
    private I18nManager i18nManager;

    @Inject
    private SettingsManager settingsManager;

    @Inject
    private MainModel model;

    @Inject
    private FxmlHandler fxmlHandler;

    @Inject
    private GuiTaskHandler guiTaskHandler;

    private BorderPane mainBorderPane;

    private SessionActions sessionActions;

    public void initialise(final BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }

    public SessionState getSessionState() {
        return model.getSessionState().orElseThrow(() -> new VocabHunterException("No session state available"));
    }

    public SessionModel addSession(final SessionState state) {
        SessionViewTool viewTool = new SessionViewTool(i18nManager);
        SessionModelTool sessionTool = new SessionModelTool(state, model.getFilterSettings(), viewTool.selectedProperty(), settingsManager.getWindowSettings().orElseGet(WindowSettings::new));
        SessionModel sessionModel = sessionTool.buildModel();
        ControllerAndView<SessionController, Node> cav = fxmlHandler.loadControllerAndView(ViewFxml.SESSION);
        SessionController controller = cav.getController();

        controller.initialise(guiTaskHandler, sessionModel);
        viewTool.setTabContent(SessionTab.ANALYSIS, cav.getView());
        viewTool.setTabContent(SessionTab.PROGRESS, progressView(sessionModel));
        mainBorderPane.setCenter(viewTool.getView());

        sessionActions = controller.getSessionActions();

        return sessionModel;
    }

    private Node progressView(final SessionModel sessionModel) {
        ControllerAndView<ProgressController, Node> cav = fxmlHandler.loadControllerAndView(ViewFxml.PROGRESS);

        cav.getController().initialise(sessionModel.getProgress());

        return cav.getView();
    }

    public Optional<SessionActions> getSessionActions() {
        return Optional.ofNullable(sessionActions);
    }
}
