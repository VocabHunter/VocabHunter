/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Singleton;

@Singleton
public class ThreadPoolToolImpl implements ThreadPoolTool {
    @Override
    public ExecutorService singleDaemonExecutor(final String name) {
        return Executors.newFixedThreadPool(1, r -> newDaemonThread(r, name));
    }

    @Override
    public ExecutorService daemonExecutor(final String name, final int threadCount) {
        AtomicInteger idGenerator = new AtomicInteger(1);

        return Executors.newFixedThreadPool(threadCount, r -> newDaemonThread(r, name, idGenerator));
    }

    private Thread newDaemonThread(final Runnable r, final String name, final AtomicInteger nextId) {
        return newDaemonThread(r, String.format("%s %d", name, nextId.getAndIncrement()));
    }

    private Thread newDaemonThread(final Runnable r, final String name) {
        Thread thread = new Thread(r, name);

        thread.setDaemon(true);

        return thread;
    }
}
