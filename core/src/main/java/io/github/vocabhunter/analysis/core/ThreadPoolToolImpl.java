/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Singleton;

@Singleton
public class ThreadPoolToolImpl implements ThreadPoolTool {
    private static final int GUI_POOL_SIZE = 3;

    private static final int FILTER_POOL_SIZE = 4;

    private final AtomicInteger nextGuiThreadId = new AtomicInteger(1);

    private final AtomicInteger nextFilterThreadId = new AtomicInteger(1);

    private final ScheduledExecutorService guiThreadPool = Executors.newScheduledThreadPool(GUI_POOL_SIZE, r -> newDaemonThread(r, "gui-background-worker-", nextGuiThreadId));

    private final ExecutorService filterThreadPool = Executors.newFixedThreadPool(FILTER_POOL_SIZE, r -> newDaemonThread(r, "filter-file-reader-", nextFilterThreadId));

    private final DelayedExecutor wrappedFilterThreadPool = new DelayedExecutorImpl(filterThreadPool);

    @Override
    public ScheduledExecutorService guiThreadPool() {
        return guiThreadPool;
    }

    @Override
    public DelayedExecutor filterThreadPool() {
        return wrappedFilterThreadPool;
    }

    private Thread newDaemonThread(final Runnable r, final String prefix, final AtomicInteger nextId) {
        int threadNumber = nextId.getAndIncrement();
        Thread thread = new Thread(r, prefix + threadNumber);

        thread.setDaemon(true);

        return thread;
    }

    @Override
    public void forceShutdown() {
        guiThreadPool.shutdownNow();
        filterThreadPool.shutdownNow();
    }
}
