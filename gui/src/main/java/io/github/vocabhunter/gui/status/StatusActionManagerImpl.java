/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import io.github.vocabhunter.analysis.core.ThreadPoolTool;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class StatusActionManagerImpl extends AbstractStatusActionManager {
    private static final Logger LOG = LoggerFactory.getLogger(StatusActionManagerImpl.class);

    private static final int WAIT_MILLIS = 100;

    private final ExecutorService service = ThreadPoolTool.singleDaemonExecutor("GUI Status Action");

    public StatusActionManagerImpl(final StatusManager statusManager) {
        super(statusManager);
    }

    @Override
    protected void executeHandler(final Runnable handler) {
        service.execute(() -> waitAndRun(handler));
    }

    private void waitAndRun(final Runnable handler) {
        try {
            Thread.sleep(WAIT_MILLIS);
        } catch (InterruptedException e) {
            LOG.debug("Thread woken unexpectedly", e);
        }
        Platform.runLater(handler::run);
    }
}
