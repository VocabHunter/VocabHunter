/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import io.github.vocabhunter.analysis.session.FileNameTool;
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

import static javafx.beans.binding.Bindings.*;

@Singleton
public class StatusManagerImpl implements StatusManager {
    private static final Logger LOG = LoggerFactory.getLogger(StatusManagerImpl.class);

    private static final String NAME_NEW_SESSION = "Start a new VocabHunter session";

    private static final String NAME_OPEN_SESSION = "Open a saved VocabHunter session";

    private static final String NAME_SAVE_SESSION = "Save the VocabHunter session";

    private static final String NAME_EXPORT = "Export the words marked as unknown";

    private static final String NAME_EXIT = "Exit VocabHunter";

    private static final String NAME_ABOUT = "About VocabHunter";

    private static final int POSITION_BUFFER_SIZE = 100;

    private String name;

    private final SimpleBooleanProperty sessionAvailable = new SimpleBooleanProperty();

    private final SimpleBooleanProperty busy = new SimpleBooleanProperty();

    private final SimpleStringProperty positionDescription = new SimpleStringProperty();

    private final SimpleStringProperty actionDescription = new SimpleStringProperty();

    private final SimpleDoubleProperty markedPercentage = new SimpleDoubleProperty();

    private final SimpleStringProperty graphText = new SimpleStringProperty();

    private final AtomicBoolean gatekeeper = new AtomicBoolean();

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
        return begin(NAME_NEW_SESSION);
    }

    @Override
    public boolean beginOpenSession() {
        return begin(NAME_OPEN_SESSION);
    }

    @Override
    public boolean beginSaveSession() {
        return begin(NAME_SAVE_SESSION);
    }

    @Override
    public boolean beginExport() {
        return begin(NAME_EXPORT);
    }

    @Override
    public boolean beginExit() {
        return begin(NAME_EXIT);
    }

    @Override
    public boolean beginAbout() {
        return begin(NAME_ABOUT);
    }

    private boolean begin(final String name) {
        if (gatekeeper.compareAndSet(false, true)) {
            this.name = name;
            LOG.debug("Begin: {}", name);
            actionDescription.setValue(name);
            busy.setValue(true);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void performAction(final Path file) {
        LOG.debug("Perform: {}", name);
        actionDescription.setValue(String.format("%s: '%s'...", name, FileNameTool.filename(file)));
    }

    @Override
    public void markSuccess() {
        LOG.debug("Success: {}", name);
    }

    @Override
    public void completeAction() {
        LOG.debug("Complete: {}", name);
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
