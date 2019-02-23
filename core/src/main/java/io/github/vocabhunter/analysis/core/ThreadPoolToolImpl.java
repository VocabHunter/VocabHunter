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

    private static final AtomicInteger NEXT_GUI_THREAD_ID = new AtomicInteger(1);

    private final AtomicInteger nextFilterThreadId = new AtomicInteger(1);

    public static final ScheduledExecutorService GUI_THREAD_POOL = Executors.newScheduledThreadPool(GUI_POOL_SIZE, r -> newDaemonThread(r, "gui-background-worker-", NEXT_GUI_THREAD_ID));

    private final ExecutorService filterThreadPool = Executors.newFixedThreadPool(FILTER_POOL_SIZE, r -> newDaemonThread(r, "filter-file-reader-", nextFilterThreadId));

    private final DelayedExecutor wrappedFilterThreadPool = new DelayedExecutorImpl(filterThreadPool);

    @Override
    public ScheduledExecutorService guiThreadPool() {
        return GUI_THREAD_POOL;
    }

    @Override
    public DelayedExecutor filterThreadPool() {
        return wrappedFilterThreadPool;
    }

    private static Thread newDaemonThread(final Runnable r, final String prefix, final AtomicInteger nextId) {
        int threadNumber = nextId.getAndIncrement();
        Thread thread = new Thread(r, prefix + threadNumber);

        thread.setDaemon(true);

        return thread;
    }

    @Override
    public void forceShutdown() {
        GUI_THREAD_POOL.shutdownNow();
        filterThreadPool.shutdownNow();
    }
}
