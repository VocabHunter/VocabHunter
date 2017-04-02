/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GuiTaskHandlerImpl implements GuiTaskHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GuiTaskHandlerImpl.class);

    private static final int WAIT_MILLIS = 100;

    private final ExecutorService executorService;

    private final Consumer<Runnable> guiThreadRunner;

    @Inject
    public GuiTaskHandlerImpl(final ThreadPoolTool threadPoolTool) {
        this(threadPoolTool, Platform::runLater);
    }

    public GuiTaskHandlerImpl(final ThreadPoolTool threadPoolTool, final Consumer<Runnable> guiThreadRunner) {
        this.executorService = threadPoolTool.singleDaemonExecutor("GUI Background Task");
        this.guiThreadRunner = guiThreadRunner;
    }

    @Override
    public void executeInBackground(final Runnable task) {
        executorService.execute(task);
    }

    @Override
    public void executeOnGuiThread(final Runnable task) {
        guiThreadRunner.accept(task);
    }

    @Override
    public void pauseThenExecuteOnGuiThread(final Runnable task) {
        executorService.execute(() -> waitAndRun(task));
    }

    private void waitAndRun(final Runnable task) {
        try {
            Thread.sleep(WAIT_MILLIS);
        } catch (final InterruptedException e) {
            LOG.debug("Thread woken unexpectedly", e);
        }
        executeOnGuiThread(task);
    }
}
