/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import java.util.concurrent.Executor;

public interface DelayedExecutor extends Executor {
    void beginExecution();
}
