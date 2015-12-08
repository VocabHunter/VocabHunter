/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.event;

import io.github.vocabhunter.analysis.core.VocabHunterException;

import javafx.application.Platform;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is a singleton to work around some limitations of the JavaFX startup mechanism.
 */
public final class ExternalEventBroker implements ExternalEventListener, ExternalEventSource {
    private static final ExternalEventBroker instance = new ExternalEventBroker();

    private final BlockingQueue<ExternalOpenFileEvent> openFileEvents = new LinkedBlockingQueue<>();

    public static ExternalEventBroker getInstance() {
        return instance;
    }

    private ExternalEventBroker() {
        // Prevent instantiation - this class is a singleton (see Javadoc for explanation).
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
        ExecutorService executorService = Executors.newFixedThreadPool(1, this::newDaemonThread);

        executorService.submit(() -> refireEvents(listener));
    }

    private Thread newDaemonThread(final Runnable r) {
        Thread thread = new Thread(r);

        thread.setDaemon(true);

        return thread;
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
