/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import io.github.vocabhunter.gui.model.PositionModel;
import io.github.vocabhunter.gui.model.ProgressModel;

import java.nio.file.Path;

public interface StatusManager {
    boolean beginNewSession();

    boolean beginOpenSession();

    boolean beginSaveSession();

    boolean beginExport();

    boolean beginExit();

    boolean beginAbout();

    void performAction(Path file);

    void markSuccess();

    void completeAction();

    void replaceSession(PositionModel position, ProgressModel progress);
}
