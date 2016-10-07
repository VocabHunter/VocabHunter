/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import javafx.application.Platform;

import java.util.concurrent.ExecutorService;

public class GuiTaskHandlerImpl implements GuiTaskHandler {
    private final ExecutorService executorService = ThreadPoolTool.singleDaemonExecutor("GUI Background Task");

    @Override
    public void executeInBackground(final Runnable task) {
        executorService.execute(task);
    }

    @Override
    public void executeOnGuiThread(final Runnable task) {
        Platform.runLater(task);
    }
}
