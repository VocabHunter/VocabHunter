/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.common;

public interface GuiTaskHandler {
    void executeInBackground(Runnable task);

    void executeOnGuiThread(Runnable task);

    void pauseThenExecuteOnGuiThread(Runnable task);
}
