/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class GuiTaskHandlerImpl implements GuiTaskHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GuiTaskHandlerImpl.class);

    private static final int WAIT_MILLIS = 100;

    private final ExecutorService executorService = ThreadPoolTool.singleDaemonExecutor("GUI Background Task");

    @Override
    public void executeInBackground(final Runnable task) {
        executorService.execute(task);
    }

    @Override
    public void executeOnGuiThread(final Runnable task) {
        Platform.runLater(task);
    }

    @Override
    public void pauseThenExecuteOnGuiThread(final Runnable task) {
        executorService.execute(() -> waitAndRun(task));
    }

    private void waitAndRun(final Runnable task) {
        try {
            Thread.sleep(WAIT_MILLIS);
        } catch (InterruptedException e) {
            LOG.debug("Thread woken unexpectedly", e);
        }
        executeOnGuiThread(task);
    }
}
