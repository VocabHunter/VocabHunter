/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import java.util.concurrent.Executor;

public interface ThreadPoolTool {
    Executor singleDaemonExecutor(String name);

    DelayedExecutor delayedExecutor(String name, int threadCount);

    void forceShutdown();
}
