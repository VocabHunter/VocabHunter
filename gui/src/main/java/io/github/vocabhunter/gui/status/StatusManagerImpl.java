/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import io.github.vocabhunter.analysis.session.FileNameTool;
import io.github.vocabhunter.gui.i18n.I18nKey;
import io.github.vocabhunter.gui.i18n.I18nManager;
import io.github.vocabhunter.gui.model.PositionModel;
import io.github.vocabhunter.gui.model.ProgressModel;
import io.github.vocabhunter.gui.model.StatusModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
import javax.inject.Singleton;

import static io.github.vocabhunter.gui.i18n.I18nKey.*;
import static javafx.beans.binding.Bindings.*;

@Singleton
public class StatusManagerImpl implements StatusManager {
    private static final Logger LOG = LoggerFactory.getLogger(StatusManagerImpl.class);

    private final I18nManager i18nManager;

    private I18nKey currentAction;

    private static final int POSITION_BUFFER_SIZE = 100;

    private final SimpleBooleanProperty sessionAvailable = new SimpleBooleanProperty();

    private final SimpleBooleanProperty busy = new SimpleBooleanProperty();

    private final SimpleStringProperty positionDescription = new SimpleStringProperty();

    private final SimpleStringProperty actionDescription = new SimpleStringProperty();

    private final SimpleDoubleProperty markedPercentage = new SimpleDoubleProperty();

    private final SimpleStringProperty graphText = new SimpleStringProperty();

    private final AtomicBoolean gatekeeper = new AtomicBoolean();

    @Inject
    public StatusManagerImpl(final I18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    @Inject
    public void setStatusModel(final StatusModel model) {
        model.textProperty().bind(when(busy).then(actionDescription).otherwise(positionDescription));
        model.busyProperty().bind(busy);
        model.activityProperty().bind(when(busy).then(-1).otherwise(0));
        model.graphShownProperty().bind(and(sessionAvailable, not(busy)));
        model.markedFractionProperty().bind(divide(markedPercentage, 100));
        model.graphTextProperty().bind(graphText);
    }

    @Override
    public boolean beginNewSession() {
        return begin(STATUS_ACTION_NEW);
    }

    @Override
    public boolean beginOpenSession() {
        return begin(STATUS_ACTION_OPEN);
    }

    @Override
    public boolean beginSaveSession() {
        return begin(STATUS_ACTION_SAVE);
    }

    @Override
    public boolean beginExport() {
        return begin(STATUS_ACTION_EXPORT);
    }

    @Override
    public boolean beginExit() {
        return begin(STATUS_ACTION_EXIT);
    }

    @Override
    public boolean beginAbout() {
        return begin(STATUS_ACTION_ABOUT);
    }

    private boolean begin(final I18nKey key) {
        if (gatekeeper.compareAndSet(false, true)) {
            currentAction = key;
            LOG.debug("Begin: {}", currentAction);
            actionDescription.setValue(i18nManager.text(key));
            busy.setValue(true);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void performAction(final Path file) {
        LOG.debug("Perform: {}", currentAction);
        actionDescription.setValue(String.format("%s: '%s'...", i18nManager.text(currentAction), FileNameTool.filename(file)));
    }

    @Override
    public void markSuccess() {
        LOG.debug("Success: {}", currentAction);
    }

    @Override
    public void completeAction() {
        LOG.debug("Complete: {}", currentAction);
        busy.setValue(false);
        gatekeeper.set(false);
    }

    @Override
    public void replaceSession(final PositionModel position, final ProgressModel progress) {
        positionDescription.unbind();
        positionDescription.bind(createStringBinding(() -> positionDescription(position, progress),
            position.positionIndexProperty(), position.sizeProperty(), position.analysisModeProperty(), position.editableProperty(), progress.unseenFilteredProperty()));

        markedPercentage.unbind();
        markedPercentage.bind(progress.markedPercentVisibleProperty());

        graphText.unbind();
        graphText.bind(format("%.0f%% of words marked", markedPercentage));

        sessionAvailable.set(true);
    }

    private String positionDescription(final PositionModel position, final ProgressModel progress) {
        if (position.isAnalysisMode()) {
            StringBuilder buffer = new StringBuilder(POSITION_BUFFER_SIZE);

            buffer.append(MessageFormat.format("Word {0} of {1} {1,choice,0#words|1#word|1<words}", position.getPositionIndex() + 1, position.getSize()));
            if (position.isEditable()) {
                int filtered = progress.unseenFilteredProperty().get();

                if (filtered > 0) {
                    buffer.append(MessageFormat.format(" ({0} {0,choice,0#words|1#word|1<words} hidden by filter)", filtered));
                }
            } else {
                buffer.append(" marked as unknown");
            }

            return buffer.toString();
        } else {
            return "";
        }
    }
}
