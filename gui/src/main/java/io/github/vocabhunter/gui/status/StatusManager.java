/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.gui.status;

import io.github.vocabhunter.gui.model.PositionModel;
import io.github.vocabhunter.gui.model.ProgressModel;
import io.github.vocabhunter.gui.model.StatusModel;

public interface StatusManager {
    void initialise(StatusModel model);

    void beginNewSession();

    void beginOpenSession();

    void beginSaveSession();

    void beginExport();

    void beginExit();

    void beginAbout();

    void performAction();

    void markSuccess();

    void completeAction();

    void replaceSession(PositionModel position, ProgressModel progress);
}
