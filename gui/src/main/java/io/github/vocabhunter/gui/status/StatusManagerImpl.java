/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import io.github.vocabhunter.gui.model.PositionModel;
import io.github.vocabhunter.gui.model.ProgressModel;
import io.github.vocabhunter.gui.model.StatusModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

import static javafx.beans.binding.Bindings.createStringBinding;
import static javafx.beans.binding.Bindings.when;

public class StatusManagerImpl implements StatusManager {
    private static final Logger LOG = LoggerFactory.getLogger(StatusManagerImpl.class);

    private static final String NAME_NEW_SESSION = "Start a new VocabHunter session";

    private static final String NAME_OPEN_SESSION = "Open a saved VocabHunter session";

    private static final String NAME_SAVE_SESSION = "Save the VocabHunter session";

    private static final String NAME_EXPORT = "Export the words marked as unknown";

    private static final int POSITION_BUFFER_SIZE = 100;

    private String name;

    private final SimpleBooleanProperty busy = new SimpleBooleanProperty();

    private final SimpleStringProperty positionDescription = new SimpleStringProperty();

    private final SimpleStringProperty actionDescription = new SimpleStringProperty();

    public void initialise(final StatusModel model) {
        model.textProperty().bind(when(busy).then(actionDescription).otherwise(positionDescription));
        model.busyProperty().bind(busy);
        model.progressProperty().bind(when(busy).then(-1).otherwise(0));
    }

    @Override
    public void beginNewSession() {
        name = NAME_NEW_SESSION;
        begin();
    }

    @Override
    public void beginOpenSession() {
        name = NAME_OPEN_SESSION;
        begin();
    }

    @Override
    public void beginSaveSession() {
        name = NAME_SAVE_SESSION;
        begin();
    }

    @Override
    public void beginExport() {
        name = NAME_EXPORT;
        begin();
    }

    private void begin() {
        LOG.debug("Begin: {}", name);
        actionDescription.setValue(name);
        busy.setValue(true);
    }

    @Override
    public void performAction() {
        LOG.debug("Perform: {}", name);
    }

    @Override
    public void markSuccess() {
        LOG.debug("Success: {}", name);
    }

    @Override
    public void completeAction() {
        LOG.debug("Complete: {}", name);
        busy.setValue(false);
    }

    @Override
    public void replaceSession(final PositionModel position, final ProgressModel progress) {
        positionDescription.unbind();
        positionDescription.bind(createStringBinding(() -> positionDescription(position, progress),
            position.positionIndexProperty(), position.sizeProperty(), position.analysisModeProperty(), position.editableProperty(), progress.unseenFilteredProperty()));
    }

    private String positionDescription(final PositionModel position, final ProgressModel progress) {
        if (position.isAnalysisMode()) {
            StringBuilder buffer = new StringBuilder(POSITION_BUFFER_SIZE);

            buffer.append(MessageFormat.format("Word {0} of {1} {1,choice,0#words|1#word|1<words}", position.getPositionIndex() + 1, position.getSize()));
            if (!position.isEditable()) {
                buffer.append(" marked as unknown");
            }

            int filtered = progress.unseenFilteredProperty().get();

            if (filtered > 0) {
                buffer.append(MessageFormat.format(" ({0} {0,choice,0#words|1#word|1<words} hidden by filter)", filtered));
            }

            return buffer.toString();
        } else {
            return "";
        }
    }
}
