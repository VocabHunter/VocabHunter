/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.event;

import io.github.vocabhunter.analysis.core.ThreadPoolTool;
import io.github.vocabhunter.analysis.core.VocabHunterException;
import javafx.application.Platform;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class ExternalEventBrokerImpl implements ExternalEventListener, ExternalEventBroker {
    private final BlockingQueue<ExternalOpenFileEvent> openFileEvents = new LinkedBlockingQueue<>();

    private final ThreadPoolTool threadPoolTool;

    @Inject
    public ExternalEventBrokerImpl(final ThreadPoolTool threadPoolTool) {
        this.threadPoolTool = threadPoolTool;
    }

    @Override
    public void fireOpenFileEvent(final ExternalOpenFileEvent event) {
        try {
            openFileEvents.put(event);
        } catch (final InterruptedException e) {
            throw new VocabHunterException("Unable to register event", e);
        }
    }

    @Override
    public void setListener(final ExternalEventListener listener) {
        ExecutorService executorService = threadPoolTool.singleDaemonExecutor("External Event Broker");

        executorService.submit(() -> refireEvents(listener));
    }

    private void refireEvents(final ExternalEventListener listener) {
        boolean isRunning = true;
        while (isRunning && !Thread.interrupted()) {
            try {
                ExternalOpenFileEvent event = openFileEvents.take();

                Platform.runLater(() -> listener.fireOpenFileEvent(event));
            } catch (final InterruptedException e) {
                isRunning = false;
            }
        }
    }
}
