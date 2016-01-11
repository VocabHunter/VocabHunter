/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadPoolTool {
    private ThreadPoolTool() {
        // Prevent instantiation - all methods are static
    }

    public static ExecutorService singleDaemonExecutor(final String name) {
        return Executors.newFixedThreadPool(1, r -> newDaemonThread(r, name));
    }

    private static Thread newDaemonThread(final Runnable r, final String name) {
        Thread thread = new Thread(r);

        thread.setDaemon(true);
        thread.setName(name);

        return thread;
    }
}
