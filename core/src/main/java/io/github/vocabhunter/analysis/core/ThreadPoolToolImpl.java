/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Singleton;

@Singleton
public class ThreadPoolToolImpl implements ThreadPoolTool {
    private final List<ExecutorService> services = new ArrayList<>();

    @Override
    public Executor singleDaemonExecutor(final String name) {
        ExecutorService executor = Executors.newFixedThreadPool(1, r -> newDaemonThread(r, name));

        services.add(executor);

        return executor;
    }

    @Override
    public DelayedExecutor delayedExecutor(final String name, final int threadCount) {
        AtomicInteger idGenerator = new AtomicInteger(1);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount, r -> newDaemonThread(r, name, idGenerator));

        services.add(executor);

        return new DelayedExecutorImpl(executor);
    }

    private Thread newDaemonThread(final Runnable r, final String name, final AtomicInteger nextId) {
        return newDaemonThread(r, String.format("%s %d", name, nextId.getAndIncrement()));
    }

    private Thread newDaemonThread(final Runnable r, final String name) {
        Thread thread = new Thread(r, name);

        thread.setDaemon(true);

        return thread;
    }

    @Override
    public void forceShutdown() {
        services.forEach(ExecutorService::shutdownNow);
    }
}
