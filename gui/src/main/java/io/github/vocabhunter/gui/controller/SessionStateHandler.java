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
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionStateHandler {
    @Inject
    private SettingsManager settingsManager;

    @Inject
    private MainModel model;

    @Inject
    private SessionProvider sessionProvider;

    @Inject
    private ProgressProvider progressProvider;

    private BorderPane mainBorderPane;

    private EventHandler<KeyEvent> keyPressHandler;

    public void initialise(final BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }

    public SessionState getSessionState() {
        return model.getSessionState().orElseThrow(() -> new VocabHunterException("No session state available"));
    }

    public SessionModel addSession(final SessionState state) {
        SessionViewTool viewTool = new SessionViewTool();
        SessionModelTool sessionTool = new SessionModelTool(state, model.getFilterSettings(), viewTool.selectedProperty(), settingsManager.getWindowSettings().orElseGet(WindowSettings::new));
        SessionModel sessionModel = sessionTool.buildModel();
        ControllerAndView<SessionController, Node> cav = sessionProvider.get();
        SessionController controller = cav.getController();

        controller.initialise(sessionModel);
        viewTool.setTabContent(SessionTab.ANALYSIS, cav.getView());
        viewTool.setTabContent(SessionTab.PROGRESS, progressView(sessionModel));
        mainBorderPane.setCenter(viewTool.getView());

        keyPressHandler = controller.getKeyPressHandler();

        return sessionModel;
    }

    private Node progressView(final SessionModel sessionModel) {
        ControllerAndView<ProgressController, Node> cav = progressProvider.get();

        cav.getController().initialise(sessionModel.getProgress());

        return cav.getView();
    }

    public Optional<EventHandler<KeyEvent>> getKeyPressHandler() {
        return Optional.ofNullable(keyPressHandler);
    }
}
