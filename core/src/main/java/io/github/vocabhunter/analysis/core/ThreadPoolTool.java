/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import java.util.concurrent.ExecutorService;


public interface ThreadPoolTool {
    ExecutorService singleDaemonExecutor(String name);

    ExecutorService daemonExecutor(String name, int threadCount);
}
