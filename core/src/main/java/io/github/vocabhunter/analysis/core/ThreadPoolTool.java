/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import java.util.concurrent.ScheduledExecutorService;

public interface ThreadPoolTool {
    ScheduledExecutorService guiThreadPool();

    DelayedExecutor filterThreadPool();

    void forceShutdown();
}
