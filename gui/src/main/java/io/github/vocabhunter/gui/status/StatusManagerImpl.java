/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import io.github.vocabhunter.gui.model.StatusModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusManagerImpl implements StatusManager {
    private static final Logger LOG = LoggerFactory.getLogger(StatusManagerImpl.class);

    private static final String NAME_NEW_SESSION = "Start a new VocabHunter session";

    private static final String NAME_OPEN_SESSION = "Open a saved VocabHunter session";

    private static final String NAME_SAVE_SESSION = "Save the VocabHunter session";

    private static final String NAME_EXPORT = "Export the words marked as unknown";

    private String name;

    private StatusModel model;

    public void initialise(final StatusModel model) {
        this.model = model;
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
        model.setText(name);
        model.setProgress(-1);
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
        model.setText("");
        model.setProgress(0);
    }
}
