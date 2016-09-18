/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

@FunctionalInterface
public interface StatusActionService {
    void updateStatusThenRun(Runnable runnable);
}
