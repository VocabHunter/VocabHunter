/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

import javax.inject.Singleton;

@Singleton
public class GuiTaskHandlerForTesting implements GuiTaskHandler {
    @Override
    public void executeInBackground(final Runnable task) {
        task.run();
    }

    @Override
    public void executeOnGuiThread(final Runnable task) {
        task.run();
    }

    @Override
    public void pauseThenExecuteOnGuiThread(final Runnable task) {
        task.run();
    }
}
