/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.controller;

import io.github.vocabhunter.analysis.core.VocabHunterException;
import io.github.vocabhunter.analysis.session.SessionState;
import io.github.vocabhunter.gui.common.ControllerAndView;
import io.github.vocabhunter.gui.model.MainModel;
import io.github.vocabhunter.gui.model.SessionModel;
import io.github.vocabhunter.gui.settings.SettingsManager;
import io.github.vocabhunter.gui.settings.WindowSettings;
import io.github.vocabhunter.gui.view.SessionTab;
import io.github.vocabhunter.gui.view.SessionViewTool;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.util.Optional;

public class SessionStateHandler {
    private final BorderPane mainBorderPane;

    private final GuiFactory factory;

    private final SettingsManager settingsManager;

    private final MainModel model;

    private EventHandler<KeyEvent> keyPressHandler;

    public SessionStateHandler(final BorderPane mainBorderPane, final GuiFactory factory, final SettingsManager settingsManager, final MainModel model) {
        this.mainBorderPane = mainBorderPane;
        this.factory = factory;
        this.settingsManager = settingsManager;
        this.model = model;
    }

    public SessionState getSessionState() {
        return model.getSessionState().orElseThrow(() -> new VocabHunterException("No session state available"));
    }

    public SessionModel addSession(final SessionState state) {
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

    public Optional<EventHandler<KeyEvent>> getKeyPressHandler() {
        return Optional.ofNullable(keyPressHandler);
    }
}
