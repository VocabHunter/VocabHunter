/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.core;

public class GuiTaskHandlerForTesting implements GuiTaskHandler {
    @Override
    public void executeInBackground(final Runnable task) {
        task.run();
    }

    @Override
    public void executeOnGuiThread(final Runnable task) {
        task.run();
    }
}
